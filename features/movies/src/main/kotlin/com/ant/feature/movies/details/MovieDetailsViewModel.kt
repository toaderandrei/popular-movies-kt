package com.ant.feature.movies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.movies.DeleteMovieDetailsUseCase
import com.ant.domain.usecases.movies.MovieDetailsUseCase
import com.ant.domain.usecases.movies.SaveMovieDetailsUseCase
import com.ant.models.model.Result
import com.ant.models.request.MovieAppendToResponseItem
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieDetailsUseCase: MovieDetailsUseCase,
    private val saveMovieDetailsUseCase: SaveMovieDetailsUseCase,
    private val deleteMovieDetailsUseCase: DeleteMovieDetailsUseCase,
) : ViewModel() {

    private val movieId: Long = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var toggleJob: Job? = null

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    fun toggleFavorite() {
        val details = _uiState.value.movieDetails ?: return
        val wasFavorite = _uiState.value.isFavorite
        // Optimistic UI update
        _uiState.update { it.copy(isFavorite = !wasFavorite) }
        toggleJob?.cancel()
        toggleJob = viewModelScope.launch {
            val result = if (wasFavorite) {
                deleteMovieDetailsUseCase(details)
            } else {
                saveMovieDetailsUseCase(details)
            }
            result.collect { emission ->
                if (emission is Result.Error) {
                    // Revert on failure
                    _uiState.update { it.copy(isFavorite = wasFavorite) }
                }
            }
        }
    }

    private fun loadDetails() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val request = RequestType.MovieRequestDetails(
                tmdbMovieId = movieId,
                appendToResponseItems = listOf(
                    MovieAppendToResponseItem.REVIEWS,
                    MovieAppendToResponseItem.VIDEOS,
                    MovieAppendToResponseItem.CREDITS,
                )
            )
            movieDetailsUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                movieDetails = result.data,
                                isFavorite = result.data.movieData.favored == true,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.throwable.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
