package com.ant.models.source.repositories.favorites

import com.ant.models.request.RequestType
import com.ant.models.source.datasource.movies.SaveAsFavoriteDataSource
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteDetailsToRemoteRepository @Inject constructor(
    private val updateMovieAsFavoriteDataSource: SaveAsFavoriteDataSource,
) : Repository<Repository.Params<RequestType.FavoriteRequest>, Boolean> {
    override suspend fun performRequest(params: Repository.Params<RequestType.FavoriteRequest>): Boolean {
        return updateMovieAsFavoriteDataSource.invoke(params)
    }
}