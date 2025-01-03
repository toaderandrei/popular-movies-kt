package com.ant.app.ui.compose.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ant.feature.favorites.navigation.favoritesGraph
import com.ant.feature.movies.navigation.moviesGraph
import com.ant.feature.search.navigation.searchGraph
import com.ant.feature.tvshow.navigation.tvShowGraph
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
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        moviesGraph(navController)
        tvShowGraph(navController)
        favoritesGraph(navController)
        searchGraph(navController)
    }
}