package com.ant.app.ui.extensions

import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import com.ant.models.model.TvShowListState
import com.ant.models.model.get
import com.ant.models.model.getErrorOrNull
import com.ant.models.model.isLoading
import com.ant.models.model.isSuccess
import com.ant.models.request.TvShowType

fun TvShowListState.parseResponse(
    response: Result<List<TvShow>>,
    movieType: TvShowType
): TvShowListState {
    return when (movieType) {
        TvShowType.POPULAR -> {
            parsePopularMovies(response)
        }

        TvShowType.ONTV_NOW -> {
            parseOnTvNowTvSeries(response)
        }

        TvShowType.AIRING_TODAY -> {
            parseAiringTodayTvSeries(response)
        }

        TvShowType.TOP_RATED -> {
            parseTopRatedTvSeries(response)
        }
    }
}

private fun TvShowListState.parsePopularMovies(it: Result<List<TvShow>>): TvShowListState {
    return when {
        it.isLoading -> {
            copy(
                popularTvSeries = popularTvSeries.copy(loading = true, data = null, error = null)
            )
        }

        it.isSuccess -> {
            copy(
                popularTvSeries = popularTvSeries.copy(loading = false, data = it.get(), error = null)
            )
        }

        else -> {
            copy(
                popularTvSeries = popularTvSeries.copy(
                    loading = false,
                    data = null,
                    error = it.getErrorOrNull(),
                )
            )
        }
    }
}

private fun TvShowListState.parseOnTvNowTvSeries(result: Result<List<TvShow>>): TvShowListState {
    return when {
        result.isLoading -> {
            copy(
                onTvNow = onTvNow.copy(loading = true, data = null, error = null)
            )
        }

        result.isSuccess -> {
            copy(
                onTvNow = onTvNow.copy(loading = false, data = result.get(), error = null)
            )
        }

        else -> {
            copy(
                onTvNow = onTvNow.copy(
                    loading = false,
                    data = null,
                    error = result.getErrorOrNull(),
                )
            )
        }
    }
}

private fun TvShowListState.parseTopRatedTvSeries(result: Result<List<TvShow>>): TvShowListState {
    return when {
        result.isLoading -> {
            copy(
                topRated = topRated.copy(loading = true)
            )
        }

        result.isSuccess -> {
            copy(
                topRated = topRated.copy(loading = false, data = result.get())
            )
        }

        else -> {
            copy(
                topRated = topRated.copy(
                    loading = false,
                    data = null,
                    error = result.getErrorOrNull(),
                )
            )
        }
    }
}

private fun TvShowListState.parseAiringTodayTvSeries(result: Result<List<TvShow>>): TvShowListState {
    return when {
        result.isLoading -> {
            copy(
                airingToday = airingToday.copy(loading = true)
            )
        }

        result.isSuccess -> {
            copy(
                airingToday = airingToday.copy(loading = false, data = result.get())
            )
        }

        else -> {
            copy(
                airingToday = airingToday.copy(
                    loading = false,
                    error = result.getErrorOrNull(),
                )
            )
        }
    }
}