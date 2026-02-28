package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.FavoriteRepository
import com.ant.data.repositories.TvSeriesRepository
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
    private val tvSeriesRepository: TvSeriesRepository,
    private val favoriteRepository: FavoriteRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: TvShowDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            tvSeriesRepository.saveTvSeriesDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        try {
                            val synced = favoriteRepository.syncFavoriteToRemote(
                                RequestType.FavoriteRequest(
                                    sessionId = sessionId,
                                    favorite = true,
                                    favoriteId = parameters.tvSeriesData.id.toInt(),
                                    mediaType = FavoriteType.TV
                                )
                            )
                            if (synced) {
                                favoriteRepository.updateSyncStatus(
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
