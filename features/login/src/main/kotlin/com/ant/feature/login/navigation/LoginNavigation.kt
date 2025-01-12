package com.ant.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ant.feature.login.LoginScreen
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.LoginScreenDestination

fun NavGraphBuilder.loginGraph(navController: NavHostController) {
    composable(LoginScreenDestination.LOGIN.route) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTHENTICATION) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onLoginCanceled = {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTHENTICATION) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}