package com.ant.app.ui.details.movies

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.common.logger.TmdbLogger
import com.ant.domain.usecases.movies.DeleteMovieDetailsUseCase
import com.ant.domain.usecases.movies.MovieDetailsUseCase
import com.ant.domain.usecases.movies.SaveMovieDetailsUseCase
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
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
) : BaseViewModel<MovieDetailsState>(MovieDetailsState()) {
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
                parseResponse(it)
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

    private fun MovieDetailsState.parseResponse(it: Result<MovieDetails>) = if (it.isLoading) {
        copy(loading = true, movieDetails = null, error = null)
    } else if (it.isSuccess) {
        copy(loading = false, movieDetails = it.get(), error = null)
    } else {
        copy(loading = false, movieDetails = null, error = (it as Result.Error).throwable)
    }
}