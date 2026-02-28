package com.ant.data.repositories.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.request.RequestType
import com.ant.models.request.toMovieId
import com.ant.database.database.MoviesDb
import com.ant.network.datasource.movies.MovieDetailsExtendedSummaryDataSource
import com.ant.network.datasource.movies.MovieDetailsLocalDbDataSource
import com.ant.network.mappers.movies.MovieDetailsMapper
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoadMovieDetailsSummaryRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val moviesDb: MoviesDb,
    private val movieDetailsMapper: MovieDetailsMapper,
) {
    suspend fun performRequest(params: RequestType.MovieRequestDetails): MovieDetails {
        val movieData = moviesDb.moviesDao().findMovieById(params.toMovieId())
        return if (movieData != null) {
            MovieDetailsLocalDbDataSource(
                movieData,
                moviesDb
            ).invoke()
        } else MovieDetailsExtendedSummaryDataSource(
            params,
            tmdbService,
            movieDetailsMapper
        ).invoke()
    }
}