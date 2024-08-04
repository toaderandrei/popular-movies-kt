package com.ant.app.ui.extensions

import com.ant.app.ui.main.base.BaseViewModelMoviesList.Companion.FIRST_PAGE
import com.ant.models.entities.MovieData
import com.ant.models.model.MoviesState
import com.ant.models.model.Result

fun <T> MoviesState<T>.parseResponse(
    response: Result<T>,
    onLoading: () -> Unit = {},
    onSuccess: (data: T?) -> Unit = {},
    onError: (error: Throwable) -> Unit = {}
): MoviesState<T> {
    return when (response) {
        is Result.Success -> {
            onSuccess(response.data)
            copy(data = response.data, loading = false, error = null)
        }

        is Result.Error -> {
            onError(response.throwable)
            copy(error = response.throwable, data = null, loading = false)
        }

        Result.Loading -> {
            onLoading()
            copy(loading = true, data = null, error = null)
        }
    }
}

fun <T> MoviesState<List<T>>.updateMoviesStatePages(
    newResult: Result<List<T>>,
    oldData: List<T>?,
    page: Int,
    onLoading: () -> Unit = {},
    onSuccess: (data: List<T>?) -> Unit = {},
    onError: (error: Throwable) -> Unit = {}
): MoviesState<List<T>> {
    val result = parseResponse(
        newResult,
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
    )
    if (page != FIRST_PAGE) {
        val previousResultList = oldData?.toMutableList() ?: emptyList()
        var newResultList = result.data?.toMutableList() ?: emptyList()
        newResultList = previousResultList.plus(newResultList)
        return result.copy(data = newResultList)
    }
    return result
}