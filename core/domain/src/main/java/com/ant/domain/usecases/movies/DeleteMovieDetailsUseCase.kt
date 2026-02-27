package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.movies.DeleteMovieDetailsRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteMovieDetailsUseCase @Inject constructor(
    private val repository: DeleteMovieDetailsRepository,
    private val sessionManager: SessionManager,
    private val updateFavoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: MovieDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        updateFavoriteToRemoteRepository.performRequest(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.movieData.id.toInt(),
                                mediaType = FavoriteType.MOVIE
                            )
                        )
                    }
                }
        }
    }
}
