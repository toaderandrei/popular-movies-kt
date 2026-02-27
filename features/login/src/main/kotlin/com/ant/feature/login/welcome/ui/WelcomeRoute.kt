package com.ant.feature.login.welcome.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.login.welcome.WelcomeViewModel

@Composable
fun WelcomeRoute(
    onNavigateToLogin: () -> Unit,
    onGuestModeActivated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.guestModeActivated) {
        if (uiState.guestModeActivated) {
            onGuestModeActivated()
        }
    }

    WelcomeScreen(
        uiState = uiState,
        onLoginClick = onNavigateToLogin,
        onContinueAsGuestClick = viewModel::continueAsGuest,
        modifier = modifier,
    )
}
