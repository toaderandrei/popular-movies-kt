package com.ant.app.ui.extensions

import com.ant.models.model.MoviesState
import com.ant.models.model.Result

fun <T> MoviesState<T>.parseResponse(
    response: Result<T?>,
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