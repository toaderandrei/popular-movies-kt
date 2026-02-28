package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.FavoriteRepository
import com.ant.data.repositories.MovieRepository
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
    private val movieRepository: MovieRepository,
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: MovieDetails): Flow<Result<Unit>> {
        return resultFlow(dispatcher) {
            movieRepository.deleteMovieDetails(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        favoriteRepository.syncFavoriteToRemote(
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
