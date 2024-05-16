package com.ant.models.source.repositories.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toMovieId
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.datasource.movies.MovieDetailsExtendedSummaryDataSource
import com.ant.models.source.datasource.movies.MovieDetailsLocalDbDataSource
import com.ant.models.source.mappers.movies.MovieDetailsMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadMovieDetailsSummaryRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val moviesDb: MoviesDb,
    private val movieDetailsMapper: MovieDetailsMapper,
) : Repository<Repository.Params<RequestType.MovieRequestDetails>, MovieDetails> {
    override suspend fun fetchData(params: Repository.Params<RequestType.MovieRequestDetails>): MovieDetails {
        val movieData = moviesDb.moviesDao().findMovieById(params.request.toMovieId())
        return if (movieData != null) {
            MovieDetailsLocalDbDataSource(movieData, moviesDb).invoke()
        } else MovieDetailsExtendedSummaryDataSource(
            params,
            tmdbService,
            movieDetailsMapper
        ).invoke()
    }
}