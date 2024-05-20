package com.ant.app.ui.extensions

import com.ant.models.model.FavoritesState
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess

fun FavoritesState.parseResponse(
    responseMovies: Result<List<MovieData>>? = null,
    responseTvSeries: Result<List<TvShow>>? = null,
): FavoritesState {
    if (responseMovies != null) {
        return parseMoviesResponse(responseMovies)
    } else if (responseTvSeries != null) {
        return parseTvSeriesResponse(responseTvSeries)
    }
    return this
}

private fun FavoritesState.parseMoviesResponse(it: Result<List<MovieData>>): FavoritesState {
    return when {
        it.isLoading -> {
            copy(
                moviesFavored = moviesFavored.copy(loading = true, data = null, error =  null)
            )
        }

        it.isSuccess -> {
            copy(
                moviesFavored = moviesFavored.copy(loading = false, data = it.get(), error = null)

            )
        }

        else -> {
            copy(
                moviesFavored = moviesFavored.copy(
                    loading = false,
                    data = null,
                    error = it.getErrorOrNull(),
                )
            )
        }
    }
}

private fun FavoritesState.parseTvSeriesResponse(result: Result<List<TvShow>>): FavoritesState {
    return when {
        result.isLoading -> {
            copy(
                tvSeriesFavored = tvSeriesFavored.copy(loading = true, data = null, error = null)
            )
        }

        result.isSuccess -> {
            copy(
                tvSeriesFavored = tvSeriesFavored.copy(loading = false, data = result.get(), error = null)
            )
        }

        else -> {
            copy(
                tvSeriesFavored = tvSeriesFavored.copy(
                    loading = false,
                    data = null,
                    error = result.getErrorOrNull(),
                )
            )
        }
    }
}