package com.ant.models.source.datasource.movies

import com.ant.models.request.RequestType
import com.ant.models.source.extensions.bodyOrThrow
import com.ant.models.source.repositories.Repository
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.FavoriteMedia
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveAsFavoriteDataSource @Inject constructor(
    private val tmdb: Tmdb,
) {
    suspend fun invoke(
        params: Repository.Params<RequestType.FavoriteRequest>,
    ): Boolean {
        val accountService = tmdb.accountService()
        tmdb.sessionId = params.request.sessionId
        val accountResponse = accountService.summary()
            .awaitResponse()
            .bodyOrThrow()

        val id = accountResponse.id
        val favoriteMedia = FavoriteMedia()
        favoriteMedia.favorite = params.request.favorite
        favoriteMedia.media_id = params.request.favoriteId
        val favoriteUpdaterResponse =
            accountService
                .favorite(id, favoriteMedia)
                .awaitResponse()
                .bodyOrThrow()
        return favoriteUpdaterResponse.status_code == 200
    }
}