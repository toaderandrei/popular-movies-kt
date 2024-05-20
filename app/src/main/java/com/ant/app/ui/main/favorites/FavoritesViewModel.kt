package com.ant.app.ui.main.favorites

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.domain.usecases.movies.LoadFavoredMoviesUseCase
import com.ant.domain.usecases.tvseries.LoadFavoredTvSeriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val loadMoviesUseCase: LoadFavoredMoviesUseCase,
    private val loadTvSeriesUseCase: LoadFavoredTvSeriesUseCase,
) : BaseViewModel<FavoritesState>(FavoritesState()) {
    fun refresh() {
        loadFavoredData()
    }

    private fun loadFavoredData() {
        viewModelScope.launch {
            loadMoviesUseCase(parameters = true)
                .collectAndSetState {
                    parseResponse(
                        response = it,
                        onSuccess = {
                            copy(
                                moviesFavored = it,
                                isMoviesFavoredLoading = false
                            )
                        },
                        onLoading = {
                            copy(
                                isMoviesFavoredLoading = true,
                            )
                        }
                    ) {
                        copy(
                            isMoviesFavoredLoading = false,
                            isMoviesFavoredError = true
                        )
                    }
                }
        }

        viewModelScope.launch {
            loadTvSeriesUseCase(parameters = true)
                .collectAndSetState {
                    parseResponse(response = it,
                        onSuccess = {
                            copy(
                                tvSeriesFavored = it,
                                isTvSeriesFavoredLoading = false
                            )
                        },
                        onLoading = {
                            copy(
                                isTvSeriesFavoredLoading = true,
                            )
                        },
                        onError = {
                            copy(
                                isTvSeriesFavoredLoading = false,
                                isTvSeriesFavoredError = true
                            )
                        }
                    )
                }
        }
    }

}