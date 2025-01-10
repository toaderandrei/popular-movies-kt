package com.ant.app.ui.main.movies

import androidx.lifecycle.viewModelScope
import com.ant.ui.viewmodels.BaseViewModel
import com.ant.app.ui.extensions.parseResponse
import com.ant.domain.usecases.movies.MovieListUseCase
import com.ant.models.model.MoviesListState
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val loadMovieListUseCase: MovieListUseCase,
) : BaseViewModel<MoviesListState>(
    MoviesListState()
) {
    init {
        loadAllMovies()
    }

    fun refresh() {
        loadAllMovies()
    }

    private fun loadAllMovies() {
        viewModelScope.launch {
            coroutineScope {
                val popularMoviesDeferred = async {
                    loadMovieListUseCase(RequestType.MovieRequest(MovieType.POPULAR))
                        .collectAndSetState {
                            parseResponse(it, MovieType.POPULAR)
                        }
                }

                val topRatedDeferred = async {
                    loadMovieListUseCase(
                        RequestType.MovieRequest(MovieType.TOP_RATED),
                    ).collectAndSetState {
                        parseResponse(it, MovieType.TOP_RATED)
                    }
                }

                val nowPlayingDeferred = async {
                    loadMovieListUseCase(
                        RequestType.MovieRequest(MovieType.NOW_PLAYING),
                    ).collectAndSetState {
                        parseResponse(it, MovieType.NOW_PLAYING)
                    }
                }

                val upcomingDeferred = async {
                    loadMovieListUseCase(
                        RequestType.MovieRequest(MovieType.UPCOMING),
                    ).collectAndSetState {
                        parseResponse(it, MovieType.UPCOMING)
                    }
                }

                popularMoviesDeferred.await()
                topRatedDeferred.await()
                nowPlayingDeferred.await()
                upcomingDeferred.await()
            }
        }
    }
}