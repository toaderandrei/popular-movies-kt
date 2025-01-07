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
import com.ant.ui.navigation.MainScreenDestination

@Stable
class MainContentState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentMainScreenDestinations: MainScreenDestination?
        @Composable get() = when (currentDestination?.route) {
            MainScreenDestination.MOVIES.route -> MainScreenDestination.MOVIES
            MainScreenDestination.TV_SHOW.route -> MainScreenDestination.TV_SHOW
            //MainScreenDestination.FAVORITES.route -> MainScreenDestination.FAVORITES
            else -> null
        }

    private val destinationEntries: List<MainScreenDestination> = MainScreenDestination.entries

    val shouldShowTopAppBar: Boolean
        @Composable get() = currentMainScreenDestinations != null


    fun navigateToDestination(topLevelDestination: MainScreenDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }

        navController.navigate(topLevelDestination.route, navOptions = topLevelNavOptions)
    }

    val topLevelDestinations: List<MainScreenDestination> = destinationEntries

    fun navigateToSearch() {
        // navController.navigate(SEARCH_ROUTE)
    }
}

@Composable
fun rememberMainContentState(
    navController: NavHostController = rememberNavController()
): MainContentState {
    return remember(navController) {
        MainContentState(navController)
    }
}