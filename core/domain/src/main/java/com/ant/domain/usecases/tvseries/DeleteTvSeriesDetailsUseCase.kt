package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.tvseries.DeleteTvSeriesDetailsRepository
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTvSeriesDetailsUseCase @Inject constructor(
    private val repository: DeleteTvSeriesDetailsRepository,
    private val sessionManager: SessionManager,
    private val updateFavoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TvShowDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: TvShowDetails) {
        return repository.performRequest(parameters)
            .also {
                sessionManager.getSessionId()?.let { sessionId ->
                    updateFavoriteToRemoteRepository.performRequest(

                        RequestType.FavoriteRequest(
                            sessionId = sessionId,
                            favorite = false,
                            favoriteId = parameters.tvSeriesData.id.toInt(),
                            mediaType = FavoriteType.MOVIE
                        )
                    )
                }
            }
    }
}