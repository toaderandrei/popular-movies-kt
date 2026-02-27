package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.favorites.FavoriteDetailsToRemoteRepository
import com.ant.data.repositories.favorites.UpdateFavoriteSyncStatusRepository
import com.ant.data.repositories.movies.SaveMovieDetailsToLocalRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveMovieDetailsUseCase @Inject constructor(
    private val repository: SaveMovieDetailsToLocalRepository,
    private val favoriteToRemoteRepository: FavoriteDetailsToRemoteRepository,
    private val updateSyncStatusRepository: UpdateFavoriteSyncStatusRepository,
    private val sessionManager: SessionManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: MovieDetails): Flow<Result<Boolean>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
                .also {
                    sessionManager.getSessionId()?.let { sessionId ->
                        try {
                            val synced = favoriteToRemoteRepository.performRequest(
                                RequestType.FavoriteRequest(
                                    sessionId = sessionId,
                                    favorite = true,
                                    favoriteId = parameters.movieData.id.toInt(),
                                    mediaType = FavoriteType.MOVIE
                                )
                            )
                            if (synced) {
                                updateSyncStatusRepository.updateSyncStatus(
                                    id = parameters.movieData.id,
                                    mediaType = FavoriteType.MOVIE,
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
