package com.ant.data.repositories.search

import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.network.datasource.search.SearchTvShowDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchTvShowRepository @Inject constructor(
    private val searchTvShowDataSource: SearchTvShowDataSource,
) {
    suspend fun performRequest(params: RequestType.SearchTvShowRequest): List<TvShow> {
        return searchTvShowDataSource(params)
    }
}
