package com.ant.feature.tvshow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ant.feature.tvshow.TvShowRoute
import com.ant.feature.tvshow.details.TvShowDetailsRoute
import com.ant.ui.navigation.MainScreenDestination


fun NavGraphBuilder.tvShowNavigation(navController: NavController) {
    composable(MainScreenDestination.TV_SHOW.route) {
        TvShowRoute(
            onClick = {}
        )
    }
    composable("${MainScreenDestination.TV_SHOW.route}/details/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        id?.let {
            TvShowDetailsRoute(id = it)
        }
    }
}