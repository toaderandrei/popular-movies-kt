package com.ant.feature.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant.domain.usecases.favorites.SyncFavoriteToRemoteUseCase
import com.ant.domain.usecases.movies.LoadFavoredMoviesUseCase
import com.ant.domain.usecases.tvseries.LoadFavoredTvSeriesUseCase
import com.ant.models.model.Result
import com.ant.models.request.FavoriteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loadFavoredMoviesUseCase: LoadFavoredMoviesUseCase,
    private val loadFavoredTvSeriesUseCase: LoadFavoredTvSeriesUseCase,
    private val syncFavoriteToRemoteUseCase: SyncFavoriteToRemoteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun onTabChange(tab: FavoriteTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun refresh() {
        loadFavorites(isRefresh = true)
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun syncToRemote(id: Long, mediaType: FavoriteType) {
        viewModelScope.launch {
            syncFavoriteToRemoteUseCase(id, mediaType).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(syncingIds = it.syncingIds + id) }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                syncingIds = it.syncingIds - id,
                                snackbarMessage = null,
                            )
                        }
                        // Reload to get updated syncedToRemote status
                        loadFavorites()
                    }

                    is Result.Error -> {
                        val message = when (result.throwable) {
                            is IllegalStateException -> result.throwable.message
                            is IOException -> "Network error. Check your connection and try again"
                            else -> "Failed to sync: ${result.throwable.message}"
                        }
                        _uiState.update {
                            it.copy(
                                syncingIds = it.syncingIds - id,
                                snackbarMessage = message,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadFavorites(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            } else {
                _uiState.update { it.copy(isRefreshing = true, error = null) }
            }

            // Load favorite movies
            loadFavoredMoviesUseCase(true).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // Already handled above
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                favoriteMovies = result.data,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.throwable.message ?: "Failed to load favorite movies"
                            )
                        }
                    }
                }
            }

            // Load favorite TV shows
            loadFavoredTvSeriesUseCase(true).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // Already handled above
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                favoriteTvShows = result.data,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.throwable.message ?: "Failed to load favorite TV shows"
                            )
                        }
                    }
                }
            }
        }
    }
}
