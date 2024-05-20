package com.ant.app.ui.main.movies

import androidx.lifecycle.viewModelScope
import com.ant.app.ui.base.BaseViewModel
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.model.MoviesGroupState
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val loadMovieListUseCase: MovieListUseCase,
) : BaseViewModel<MoviesGroupState>(
    MoviesGroupState()
) {
    fun refresh() {
        loadAllMovies()
    }

    private fun loadAllMovies() {
        viewModelScope.launch {
            loadMovieListUseCase(
                Repository.Params(
                    RequestType.MovieRequest(MovieType.POPULAR),
                    1,
                )
            ).collectAndSetState {
                when {
                    it.isLoading -> {
                        copy(
                            isPopularMoviesLoading = true,
                        )
                    }

                    it.isSuccess -> {
                        copy(
                            isPopularMoviesLoading = false,
                            popularItems = it.get() ?: emptyList(),
                        )
                    }

                    else -> {
                        copy(
                            isPopularMoviesLoading = false,
                            isPopularMoviesError = true,
                            popularMoviesError = it.getErrorOrNull()
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            loadMovieListUseCase(
                Repository.Params(
                    RequestType.MovieRequest(MovieType.TOP_RATED),
                    1,
                )
            ).collectAndSetState {
                if (it.isLoading) {
                    copy(
                        isTopMoviesLoading = true,
                    )
                } else if (it.isSuccess) {
                    copy(
                        topMovieItems = it.get() ?: emptyList(),
                        isTopMoviesLoading = false
                    )
                } else {
                    copy(
                        isTopMoviesLoading = false,
                        isTopMoviesError = true,
                        topMoviesError = it.getErrorOrNull()
                    )
                }
            }
        }
        viewModelScope.launch {
            loadMovieListUseCase(
                Repository.Params(
                    RequestType.MovieRequest(MovieType.NOW_PLAYING),
                    1,
                )
            ).collectAndSetState {
                if (it.isLoading) {
                    copy(
                        isNowPlayingMoviesLoading = true,
                    )
                } else if (it.isSuccess) {
                    copy(
                        nowPlayingItems = it.get() ?: emptyList(),
                        isNowPlayingMoviesLoading = false
                    )
                } else {
                    copy(
                        isNowPlayingMoviesLoading = false,
                        isNowPlayingMoviesError = true,
                        nowPlayingMoviesError = it.getErrorOrNull()
                    )
                }
            }
        }
        viewModelScope.launch {
            loadMovieListUseCase(
                Repository.Params(
                    RequestType.MovieRequest(MovieType.UPCOMING),
                    1,
                )
            ).collectAndSetState {
                if (it.isLoading) {
                    copy(
                        isUpcomingMoviesLoading = true,
                    )
                } else if (it.isSuccess) {
                    copy(
                        upcomingItems = it.get() ?: emptyList(),
                        isUpcomingMoviesLoading = false
                    )
                } else {
                    copy(
                        isUpcomingMoviesError = true,
                        isUpcomingMoviesLoading = false,
                        upcomingMoviesError = it.getErrorOrNull()
                    )
                }
            }
        }
    }
}