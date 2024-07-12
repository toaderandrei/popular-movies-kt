package com.ant.models.model

sealed class Result<out T> {
    open fun get(): T? = null

    data class Success<T>(val data: T) : Result<T>() {
        override fun get(): T = data
    }

    data class Error<T>(val throwable: Throwable) : Result<T>()

    data object Loading : Result<Nothing>()
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

fun Result<*>.getErrorOrNull(): Throwable? {
    if (this is Result.Error) {
        return throwable
    }
    return null
}
