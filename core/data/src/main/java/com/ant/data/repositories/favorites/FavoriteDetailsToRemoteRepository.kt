package com.ant.data.repositories.favorites

import com.ant.models.request.RequestType
import com.ant.network.datasource.movies.SaveAsFavoriteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FavoriteDetailsToRemoteRepository @Inject constructor(
    private val saveMovieAsFavoriteDataSource: SaveAsFavoriteDataSource,
) {
    suspend fun performRequest(params: RequestType.FavoriteRequest): Boolean {
        return saveMovieAsFavoriteDataSource.invoke(params)
    }
}