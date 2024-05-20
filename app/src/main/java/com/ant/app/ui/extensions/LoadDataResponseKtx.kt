package com.ant.app.ui.extensions

import com.ant.app.ui.main.favorites.FavoritesState
import com.ant.models.model.LoginState
import com.ant.models.model.MoviesState
import com.ant.models.model.Result

fun <T, R> MoviesState<T>.parseResponse(
    response: Result<R>,
    onSuccess: (data: R) -> MoviesState<T>,
    onLoading: () -> MoviesState<T>,
    onError: (error: Throwable) -> MoviesState<T>
): MoviesState<T> {
    return when (response) {
        is Result.Success -> {
            onSuccess(response.data)
        }

        is Result.Error -> {
            onError(response.throwable)
        }

        Result.Loading -> {
            onLoading()
        }
    }
}

fun <R> FavoritesState.parseResponse(
    response: Result<R>,
    onSuccess: (data: R) -> FavoritesState,
    onLoading: () -> FavoritesState,
    onError: (error: Throwable) -> FavoritesState
): FavoritesState {
    return when (response) {
        is Result.Success -> {
            onSuccess(response.data)
        }

        is Result.Error -> {
            onError(response.throwable)
        }

        Result.Loading -> {
            onLoading()
        }
    }
}

fun <T, R> LoginState<T>.parseResponse(
    response: Result<R>,
    onSuccess: (data: R) -> LoginState<T>,
    onLoading: () -> LoginState<T>,
    onError: (error: Throwable) -> LoginState<T>
): LoginState<T> {
    return when (response) {
        is Result.Success -> {
            onSuccess(response.data)
        }

        is Result.Error -> {
            onError(response.throwable)
        }

        Result.Loading -> {
            onLoading()
        }
    }
}