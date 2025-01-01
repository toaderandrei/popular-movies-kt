package com.ant.app.ui.compose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.ant.feature.navigation.moviesGraph
import com.ant.ui.navigation.TopLevelDestination

@Composable
fun AppNavHost(
    appState: MoviesAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    startDestination: TopLevelDestination = TopLevelDestination.MOVIES,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        moviesGraph(appState.navController)
//        tvSeriesGraph(appState)
//        favoritesGraph(appState)
//        accountGraph(appState)
//        searchGraph(appState)
    }
}