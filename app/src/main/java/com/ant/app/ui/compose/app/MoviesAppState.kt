package com.ant.app.ui.compose.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.ant.ui.navigation.TopLevelDestination

@Stable
class MoviesAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TopLevelDestination.MOVIES.route -> TopLevelDestination.MOVIES
            TopLevelDestination.TV_SHOW.route -> TopLevelDestination.TV_SHOW
            TopLevelDestination.FAVORITES.route -> TopLevelDestination.FAVORITES
            else -> null
        }

    private val destinationEntries: List<TopLevelDestination> = TopLevelDestination.entries

    val shouldShowTopAppBar: Boolean
        @Composable get() = currentTopLevelDestination != null


    fun navigateToDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }

        navController.navigate(topLevelDestination.route, navOptions = topLevelNavOptions)
    }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToSearch() {
        // navController.navigate(SEARCH_ROUTE)
    }
}

@Composable
fun rememberMoviesAppState(
    navController: NavHostController = rememberNavController()
): MoviesAppState {
    return remember(navController) {
        MoviesAppState(navController)
    }
}