package com.ant.app.ui.extensions

import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesListState
import com.ant.models.model.Result
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.MovieType

fun MoviesListState.parseResponse(
    response: Result<List<MovieData>>,
    movieType: MovieType
): MoviesListState {
    return when (movieType) {
        MovieType.POPULAR -> {
            parsePopularMovies(response)
        }

        MovieType.NOW_PLAYING -> {
            parseNowPlayingMovies(response)
        }

        MovieType.TOP_RATED -> {
            parseTopRatedMovies(response)
        }

        MovieType.UPCOMING -> {
            parseUpcomingMoviesResponse(response)
        }
    }
}

private fun MoviesListState.parsePopularMovies(it: Result<List<MovieData>>): MoviesListState {
    return when {
        it.isLoading -> {
            copy(
                popularMovies = popularMovies.copy(loading = true, data = null, error = null)
            )
        }

        it.isSuccess -> {
            copy(
                popularMovies = popularMovies.copy(loading = false, data = it.get(), error = null)

            )
        }

        else -> {
            copy(
                popularMovies = popularMovies.copy(
                    loading = false,
                    data = null,
                    error = it.getErrorOrNull(),
                )
            )
        }
    }
}

private fun MoviesListState.parseNowPlayingMovies(result: Result<List<MovieData>>): MoviesListState {
    return when {
        result.isLoading -> {
            copy(
                nowPlayingMovies = nowPlayingMovies.copy(
                    loading = true,
                    data = null,
                    error = null
                )
            )
        }

        result.isSuccess -> {
            copy(
                nowPlayingMovies = nowPlayingMovies.copy(
                    loading = false,
                    data = result.get(),
                    error = null,
                )
            )
        }

        else -> {
            copy(
                nowPlayingMovies = nowPlayingMovies.copy(
                    loading = false,
                    error = result.getErrorOrNull(),
                    data = null,
                )
            )
        }
    }
}

private fun MoviesListState.parseTopRatedMovies(result: Result<List<MovieData>>): MoviesListState {
    return when {
        result.isLoading -> {
            copy(
                topMovies = topMovies.copy(
                    loading = true,
                    data = null,
                    error = null
                )
            )
        }

        result.isSuccess -> {
            copy(
                topMovies = topMovies.copy(
                    loading = false,
                    data = result.get(),
                    error = null
                )
            )
        }

        else -> {
            copy(
                topMovies = topMovies.copy(
                    loading = false,
                    data = null,
                    error = result.getErrorOrNull(),
                )
            )
        }
    }
}

private fun MoviesListState.parseUpcomingMoviesResponse(result: Result<List<MovieData>>): MoviesListState {
    return when {
        result.isLoading -> {
            copy(
                upcomingMovies = upcomingMovies.copy(loading = true, data = null, error = null)
            )
        }

        result.isSuccess -> {
            copy(
                upcomingMovies = upcomingMovies.copy(
                    loading = false,
                    error = null,
                    data = result.get()
                )
            )
        }

        else -> {
            copy(
                upcomingMovies = upcomingMovies.copy(
                    loading = false,
                    error = result.getErrorOrNull(),
                    data = null,
                )
            )
        }
    }
}