package com.ant.data.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.data.repositories.Repository
import com.ant.network.datasource.movies.MovieListDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadMovieListRepository @Inject constructor(
    private val movieListDataSource: MovieListDataSource,
) : Repository<RequestType.MovieRequest, List<MovieData>> {
    override suspend fun performRequest(params: RequestType.MovieRequest): List<MovieData> {
        return movieListDataSource.invoke(params)
    }
}