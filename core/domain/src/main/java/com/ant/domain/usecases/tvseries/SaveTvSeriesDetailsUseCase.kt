package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.data.repositories.tvseries.SaveTvSeriesDetailsRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveTvSeriesDetailsUseCase @Inject constructor(
    private val repository: SaveTvSeriesDetailsRepository,
    private val favoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val updateSyncStatusRepository: UpdateFavoriteSyncStatusRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: TvShowDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        try {
                            val synced = favoriteToRemoteRepository.performRequest(
                                RequestType.FavoriteRequest(
                                    sessionId = sessionId,
                                    favorite = true,
                                    favoriteId = parameters.tvSeriesData.id.toInt(),
                                    mediaType = FavoriteType.TV
                                )
                            )
                            if (synced) {
                                updateSyncStatusRepository.updateSyncStatus(
                                    id = parameters.tvSeriesData.id,
                                    mediaType = FavoriteType.TV,
                                    synced = true,
                                )
                            }
                        } catch (_: Exception) {
                            // Remote sync failed; favorite is saved locally with syncedToRemote=false
                        }
                    }
                }
        }
    }
}
