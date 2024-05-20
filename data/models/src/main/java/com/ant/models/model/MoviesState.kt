package com.ant.models.model

data class MoviesState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)

val <T> MoviesState<T>.Success: MoviesState<T>
    get() = this.copy(
        loading = false,
        data = this.data,
        error = null
    )

val <T> MoviesState<T>.Error: MoviesState<T>
    get() = this.copy(
        loading = false,
        data = null,
        error = this.error
    )

val <T> MoviesState<T>.Loading: MoviesState<T>
    get() = this.copy(
        loading = true,
        data = null,
        error = null
    )

val <T> MoviesState<T>.isLoading: Boolean
    get() = loading

val <T> MoviesState<T>.isError: Boolean
    get() = error != null

val <T> MoviesState<T>.isSuccess: Boolean
    get() = data != null

val <T> MoviesState<T>.errorMessage: String?
    get() = error?.message
