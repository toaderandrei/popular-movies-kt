package com.ant.models.model

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error<T>(val throwable: Throwable) : Result<T>
    data object Loading : Result<Nothing>
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onLoading: () -> R,
    onFailure: (exception: Throwable) -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onFailure(throwable)
        else -> onLoading()
    }
}

/**
 * Returns true if [Result] is of type [Result.Success]  and the data [Result.Success.data] is non-null.
 */
val Result<*>.isSuccess
    get() = this is Result.Success && data != null

/**
 * Returns true if [Result] if of type [Result.Error].
 */
val Result<*>.isFailure
    get() = this is Result.Error

/**
 * Returns true if the result is of type [Result.Loading].
 */
val Result<*>.isLoading
    get() = this is Result.Loading

fun <T> Result<T>.get(): T? {
    if (this is Result.Success) {
        return data
    }
    return null
}

fun Result<*>.getErrorOrNull(): Throwable? {
    if (this is Result.Error) {
        return throwable
    }
    return null
}
