package com.ant.app.ui.compose.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ant.feature.login.LoginScreen
import com.ant.feature.movies.navigation.moviesNavigation
import com.ant.feature.tvshow.navigation.tvShowNavigation
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.LoginScreenDestination
import com.ant.ui.navigation.MainScreenDestination

@Composable
fun AppNavHost(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        route = if (isUserLoggedIn) Graph.MAIN else Graph.AUTHENTICATION,
        startDestination = MainScreenDestination.MOVIES.route,
        modifier = Modifier.padding(padding)
    ) {
        loginGraph(navController)
        mainGraph(navController)
    }
}

fun NavGraphBuilder.loginGraph(navController: NavHostController) {
    navigation(
        startDestination = LoginScreenDestination.LOGIN.route,
        route = Graph.AUTHENTICATION
    ) {
        composable(LoginScreenDestination.LOGIN.route) {
            LoginScreen(onLoginSuccess = {
                navController.popBackStack()
                navController.navigate(Graph.MAIN)
            })
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        route = Graph.MAIN,
        startDestination = MainScreenDestination.MOVIES.route
    ) {
        moviesNavigation(navController)
        tvShowNavigation(navController)
    }
}