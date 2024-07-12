package com.ant.models.source.datasource.movies

import com.ant.common.logger.Logger
import com.ant.models.extensions.toMediaType
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
    private val logger: Logger,
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
        favoriteMedia.media_type = params.request.mediaType.toMediaType()
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