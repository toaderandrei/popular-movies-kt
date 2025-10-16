package com.ant.feature.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.login.LoginViewModel

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginState by viewModel.longStateFlow.collectAsStateWithLifecycle()

    LoginScreen(
        loginState = loginState,
        onLogin = viewModel::login,
        onLoginSuccess = onLoginSuccess,
        modifier = modifier,
    )
}
