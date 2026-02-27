package com.ant.network.datasource.movies

import com.ant.common.exceptions.bodyOrThrow
import com.ant.common.logger.Logger
import com.ant.models.extensions.toMediaType
import com.ant.models.request.RequestType
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.FavoriteMedia
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveAsFavoriteDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val logger: Logger,
) {
    suspend fun invoke(
        params: RequestType.FavoriteRequest,
    ): Boolean {
        val accountService = tmdb.accountService()
        tmdb.sessionId = params.sessionId
        val accountResponse = accountService.summary()
            .awaitResponse()
            .bodyOrThrow()

        val id = accountResponse.id
        val favoriteMedia = FavoriteMedia()
        favoriteMedia.favorite = params.favorite
        favoriteMedia.media_id = params.favoriteId
        favoriteMedia.media_type = params.mediaType.toMediaType()
        val favoriteUpdaterResponse =
            accountService
                .favorite(id, favoriteMedia)
                .awaitResponse()
                .bodyOrThrow()
        val result = favoriteUpdaterResponse.status_code
        logger.d("Favorite update: ${favoriteUpdaterResponse.status_message} and status code: $result")
        if (result != null) {
            return result > 0
        }
        return false
    }
}