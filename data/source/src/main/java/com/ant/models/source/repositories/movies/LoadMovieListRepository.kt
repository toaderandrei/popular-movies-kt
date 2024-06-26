package com.ant.models.source.repositories.movies

import com.ant.models.request.RequestType
import com.ant.models.entities.MovieData
import com.ant.models.source.datasource.movies.MovieListDataSource
import com.ant.models.source.mappers.movies.MoviesListMapper
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadMovieListRepository @Inject constructor(
    private val tmdbService: Tmdb,
    private val movieListMapper: MoviesListMapper,
) : Repository<Repository.Params<RequestType.MovieRequest>, List<MovieData>> {
    override suspend fun fetchData(params: Repository.Params<RequestType.MovieRequest>): List<MovieData> {
        return MovieListDataSource(params, tmdbService, movieListMapper).invoke()
    }
}