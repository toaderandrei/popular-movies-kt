package com.ant.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.login.ui.LoginRoute

const val LOGIN_ROUTE = "login"

/**
 * Navigate to the Login screen
 */
fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LOGIN_ROUTE, navOptions)
}

/**
 * Add Login screen to the navigation graph
 */
fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit,
) {
    composable(route = LOGIN_ROUTE) {
        LoginRoute(onLoginSuccess = onLoginSuccess)
    }
}
