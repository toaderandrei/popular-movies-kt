package com.ant.feature.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.model.Result
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieListUseCase: MovieListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        loadAllCategories()
    }

    fun refresh() {
        loadAllCategories(isRefresh = true)
    }

    /**
     * Load movies for all categories to display in sections
     */
    private fun loadAllCategories(isRefresh: Boolean = false) {
        // Load all main categories
        val categories = listOf(
            MovieType.POPULAR,
            MovieType.TOP_RATED,
            MovieType.NOW_PLAYING,
            MovieType.UPCOMING
        )

        if (!isRefresh) {
            _uiState.update { it.copy(isLoading = true) }
        } else {
            _uiState.update { it.copy(isRefreshing = true) }
        }

        categories.forEach { category ->
            loadCategoryMovies(category, isRefresh)
        }
    }

    /**
     * Load movies for a specific category
     */
    private fun loadCategoryMovies(category: MovieType, isRefresh: Boolean = false) {
        viewModelScope.launch {
            val request = RequestType.MovieRequest(
                movieType = category,
                page = 1
            )

            movieListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.movieSections.toMutableMap()
                            updatedSections[category] = MovieSection(
                                category = category,
                                isLoading = true
                            )
                            currentState.copy(movieSections = updatedSections)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.movieSections.toMutableMap()
                            updatedSections[category] = MovieSection(
                                category = category,
                                movies = result.data,
                                isLoading = false
                            )
                            currentState.copy(
                                movieSections = updatedSections,
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentState ->
                            val updatedSections = currentState.movieSections.toMutableMap()
                            updatedSections[category] = MovieSection(
                                category = category,
                                isLoading = false,
                                error = result.throwable.message
                            )
                            currentState.copy(
                                movieSections = updatedSections,
                                isLoading = false,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }
}
