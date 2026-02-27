package com.ant.data.repositories.search

import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.network.datasource.search.SearchMovieDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMovieRepository @Inject constructor(
    private val searchMovieDataSource: SearchMovieDataSource,
) {
    suspend fun performRequest(params: RequestType.SearchMovieRequest): List<MovieData> {
        return searchMovieDataSource(params)
    }
}
