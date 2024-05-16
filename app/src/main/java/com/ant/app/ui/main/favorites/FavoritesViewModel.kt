package com.ant.app.ui.main.favorites

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
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
                    if (it.isLoading) {
                        copy(
                            isMoviesFavoredLoading = true,
                        )
                    } else if (it.isSuccess) {
                        copy(
                            moviesFavored = it.get() ?: emptyList(),
                            isMoviesFavoredLoading = false
                        )
                    } else {
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
                    if (it.isLoading) {
                        copy(
                            isTvSeriesFavoredLoading = true,
                        )
                    } else if (it.isSuccess) {
                        copy(
                            tvSeriesFavored = it.get() ?: emptyList(),
                            isTvSeriesFavoredLoading = false
                        )
                    } else {
                        copy(
                            isTvSeriesFavoredLoading = false,
                            isTvSeriesFavoredError = true
                        )
                    }
                }
        }
    }

}