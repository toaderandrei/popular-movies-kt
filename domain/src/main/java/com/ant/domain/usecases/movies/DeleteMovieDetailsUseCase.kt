package com.ant.domain.usecases.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.source.repositories.movies.DeleteMovieDetailsRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.movies.UpdateMovieDetailsToRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteMovieDetailsUseCase @Inject constructor(
    private val repository: DeleteMovieDetailsRepository,
    private val sessionManager: SessionManager,
    private val updateFavoriteToRemoteRepository: UpdateMovieDetailsToRemoteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MovieDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: MovieDetails) {
        repository.performRequest(parameters)
            .also {
                sessionManager.getSessionId()?.let { sessionId ->
                    updateFavoriteToRemoteRepository.performRequest(
                        Repository.Params(
                            RequestType.SaveMovieRequest(
                                sessionId = sessionId,
                                favorite = false,
                                favoriteId = parameters.movieData.id.toInt()
                            )
                        )
                    )
                }
            }
    }
}