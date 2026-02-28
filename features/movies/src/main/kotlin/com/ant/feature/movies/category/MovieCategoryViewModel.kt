package com.ant.feature.movies.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.model.Result
import com.ant.models.request.MovieType
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
class MovieCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieListUseCase: MovieListUseCase,
) : ViewModel() {

    private val categoryTypeString: String = checkNotNull(savedStateHandle["categoryType"])
    private val movieType: MovieType = MovieType.valueOf(categoryTypeString)

    private val _uiState = MutableStateFlow(MovieCategoryUiState(categoryType = movieType))
    val uiState: StateFlow<MovieCategoryUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadMovies()
    }

    fun refresh() {
        loadJob?.cancel()
        _uiState.update {
            it.copy(
                movies = emptyList(),
                currentPage = 0,
                totalPages = 1,
                error = null,
            )
        }
        loadMovies(isRefresh = true)
    }

    fun loadNextPage() {
        if (!_uiState.value.canLoadMore) return
        loadMovies(page = _uiState.value.currentPage + 1)
    }

    private fun loadMovies(isRefresh: Boolean = false, page: Int = 1) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val request = RequestType.MovieRequest(movieType = movieType, page = page)
            movieListUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            when {
                                isRefresh -> it.copy(isRefreshing = true)
                                page > 1 -> it.copy(isLoadingMore = true)
                                else -> it.copy(isLoading = true)
                            }
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            val newItems = if (page > 1) {
                                (it.movies + result.data.items).distinctBy { movie -> movie.id }
                            } else {
                                result.data.items
                            }
                            it.copy(
                                movies = newItems,
                                currentPage = result.data.page,
                                totalPages = result.data.totalPages,
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = result.throwable.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
