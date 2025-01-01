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
        @Composable get() = destinationEntries.find { it.route == currentDestination?.route }


    private val destinationEntries: List<TopLevelDestination> = TopLevelDestination.entries

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