package com.ant.feature.login.state

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data object Success : LoginState
    data object Canceled: LoginState
    data class Error(val error: Throwable? = null) : LoginState
}

val LoginState.isLoading
    get() = this is LoginState.Loading

val LoginState.isSuccess
    get() = this is LoginState.Success

val LoginState.isError
    get() = this is LoginState.Error

val LoginState.isCanceled
    get() = this is LoginState.Canceled

fun LoginState.getErrorOrNull(): Throwable? {
    if (this is LoginState.Error) {
        return error
    }
    return null
}
