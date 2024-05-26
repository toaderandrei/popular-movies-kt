package com.ant.app.ui.details.movies

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.DeleteMovieDetailsUseCase
import com.ant.domain.usecases.movies.MovieDetailsUseCase
import com.ant.domain.usecases.movies.SaveMovieDetailsUseCase
import com.ant.models.entities.MovieDetails
import com.ant.models.model.MoviesState
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsMovieViewModel @Inject constructor(
    val logger: TmdbLogger,
    val movieDetailsUseCase: MovieDetailsUseCase,
    val movieSaveDetailsUseCase: SaveMovieDetailsUseCase,
    val movieDeleteDetailsUseCase: DeleteMovieDetailsUseCase,
) : BaseViewModel<MoviesState<MovieDetails>>(MoviesState()) {
    fun loadMovieDetails(movieId: Long) {
        viewModelScope.launch {
            movieDetailsUseCase(
                Repository.Params(
                    RequestType.MovieRequestDetails(
                        tmdbMovieId = movieId, appendToResponseItems = mutableListOf(
                            MovieAppendToResponseItem.REVIEWS,
                            MovieAppendToResponseItem.VIDEOS,
                            MovieAppendToResponseItem.CREDITS,
                        )
                    )
                )
            ).collectAndSetState {
                logger.d("state: $this")
                parseResponse(response = it)
            }
        }
    }

    fun retry(movieId: Long) {
        loadMovieDetails(movieId)
    }

    fun saveToDatabase(item: MovieDetails) {
        viewModelScope.launch {
            movieSaveDetailsUseCase(item).collect {
                logger.d("Saved movie details to db.")
            }
        }
    }

    fun deleteFromDatabase(item: MovieDetails) {
        viewModelScope.launch {
            movieDeleteDetailsUseCase(item).collect {
                logger.d("Delete movie details from db.")
            }
        }
    }
}