package com.ant.feature.login.account

data class AccountUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val username: String? = null,
    val sessionId: String? = null,
    val isLoggingOut: Boolean = false,
    val logoutSuccess: Boolean = false,
    val error: String? = null,
)
