package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.source.repositories.tvseries.DeleteTvSeriesDetailsRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.models.source.repositories.movies.DeleteMovieDetailsRepository
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
                        Repository.Params(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.tvSeriesData.id.toInt(),
                                mediaType = FavoriteType.MOVIE
                            )
                        )
                    )
                }
            }
    }
}