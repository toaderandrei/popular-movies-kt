package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.tvseries.SaveTvSeriesDetailsRepository
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveTvSeriesDetailsUseCase @Inject constructor(
    private val repository: SaveTvSeriesDetailsRepository,
    private val favoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TvShowDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: TvShowDetails): Unit {
        return repository.performRequest(parameters)
            .also {
                sessionManager.getSessionId()?.let { sessionId ->
                    favoriteToRemoteRepository.performRequest(
                        RequestType.FavoriteRequest(
                            sessionId = sessionId,
                            favorite = true,
                            favoriteId = parameters.tvSeriesData.id.toInt(),
                            mediaType = FavoriteType.MOVIE
                        )
                    )
                }
            }
    }
}