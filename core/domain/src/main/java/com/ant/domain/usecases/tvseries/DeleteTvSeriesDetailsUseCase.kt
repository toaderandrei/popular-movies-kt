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

class DeleteTvSeriesDetailsUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: TvShowDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            tvSeriesRepository.deleteTvSeriesDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        favoriteRepository.syncFavoriteToRemote(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.tvSeriesData.id.toInt(),
                                mediaType = FavoriteType.TV
                            )
                        )
                    }
                }
        }
    }
}
