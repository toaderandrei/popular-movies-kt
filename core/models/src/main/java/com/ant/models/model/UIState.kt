package com.ant.models.model

data class UIState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)

val <T> UIState<T>.Success: UIState<T>
    get() = this.copy(
        loading = false,
        data = this.data,
        error = null
    )

val <T> UIState<T>.Error: UIState<T>
    get() = this.copy(
        loading = false,
        data = null,
        error = this.error
    )

val <T> UIState<T>.Loading: UIState<T>
    get() = this.copy(
        loading = true,
        data = null,
        error = null
    )

val <T> UIState<T>.isLoading: Boolean
    get() = loading

val <T> UIState<T>.isError: Boolean
    get() = error != null

val <T> UIState<T>.isSuccess: Boolean
    get() = data != null

val <T> UIState<T>.errorMessage: String?
    get() = error?.message
