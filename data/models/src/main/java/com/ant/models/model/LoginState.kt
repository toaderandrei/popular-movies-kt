package com.ant.models.model

data class LoginState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)

val <T> LoginState<T>.Success: LoginState<T>
    get() = this.copy(
        loading = false,
        data = this.data,
        error = null
    )

val <T> LoginState<T>.Error: LoginState<T>
    get() = this.copy(
        loading = false,
        data = null,
        error = this.error
    )

val <T> LoginState<T>.Loading: LoginState<T>
    get() = this.copy(
        loading = true,
        data = null,
        error = null
    )