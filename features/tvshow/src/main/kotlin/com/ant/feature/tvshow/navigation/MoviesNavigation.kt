package com.ant.feature.tvshow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ant.feature.tvshow.TvShowRoute
import com.ant.feature.tvshow.details.TvShowDetailsRoute
import com.ant.ui.navigation.TopLevelDestination


fun NavController.navigateToMovies(navOptions: NavOptions) {}

fun NavGraphBuilder.tvShowGraph(navController: NavController) {
    navigation(
        startDestination = "tvshow/home",
        route = TopLevelDestination.TV_SHOW.route,
    ) {
        composable("tvshow/home") {
            TvShowRoute(
                onNavigateToDetails = { id ->
                    navController.navigate("tvshow/details/$id")
                },
                onNavigateToExpandedList = { type ->
                    navController.navigate("tvshow/list/$type")
                }
            )
        }
        composable("tvshow/details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            id?.let {
                TvShowDetailsRoute(id = it)
            }
        }
    }
}