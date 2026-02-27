package com.ant.domain.usecases.favorites

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncFavoriteToRemoteUseCase @Inject constructor(
    private val favoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val updateSyncStatusRepository: UpdateFavoriteSyncStatusRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: Long, mediaType: FavoriteType): Flow<Result<Boolean>> {
        return resultFlow(dispatcher) {
            val sessionId = sessionManager.getSessionId()
                ?: throw IllegalStateException("Login required to sync favorites to TMDb")

            val synced = favoriteToRemoteRepository.performRequest(
                RequestType.FavoriteRequest(
                    sessionId = sessionId,
                    favorite = true,
                    favoriteId = id.toInt(),
                    mediaType = mediaType,
                )
            )

            if (synced) {
                updateSyncStatusRepository.updateSyncStatus(
                    id = id,
                    mediaType = mediaType,
                    synced = true,
                )
            }

            synced
        }
    }
}
