package com.ant.data.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.RequestType
import com.ant.network.datasource.movies.MovieListDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoadMovieListRepository @Inject constructor(
    private val movieListDataSource: MovieListDataSource,
) {
    suspend fun performRequest(params: RequestType.MovieRequest): PaginatedResult<MovieData> {
        return movieListDataSource.invoke(params)
    }
}