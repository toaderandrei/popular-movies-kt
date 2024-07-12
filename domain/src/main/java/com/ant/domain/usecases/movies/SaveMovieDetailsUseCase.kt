package com.ant.domain.usecases.movies

import com.ant.domain.qualifiers.IoDispatcher
import com.ant.models.entities.MovieDetails
import com.ant.models.source.repositories.movies.SaveMovieDetailsToLocalRepository
import com.ant.domain.usecases.UseCase
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.favorites.FavoriteDetailsToRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveMovieDetailsUseCase @Inject constructor(
    private val repository: SaveMovieDetailsToLocalRepository,
    private val favoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MovieDetails, Boolean>(dispatcher) {
    override suspend fun execute(parameters: MovieDetails): Boolean {
        return repository.performRequest(parameters)
            .also {
                sessionManager.getSessionId()?.let { sessionId ->
                    favoriteToRemoteRepository.performRequest(
                        Repository.Params(
                            RequestType.FavoriteRequest(
                                sessionId = sessionId,
                                favorite = true,
                                favoriteId = parameters.movieData.id.toInt(),
                                mediaType = FavoriteType.MOVIE
                            )
                        )
                    )
                }
            }
    }
}
