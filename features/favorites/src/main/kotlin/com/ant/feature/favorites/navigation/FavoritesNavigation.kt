package com.ant.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ant.feature.favorites.FavoritesRoute
import com.ant.feature.favorites.details.FavoritesDetailsRoute
import com.ant.ui.navigation.TopLevelDestination


fun NavController.navigateToMovies(navOptions: NavOptions) {}

fun NavGraphBuilder.favoritesGraph(navController: NavController) {
    navigation(
        startDestination = "favorites/home",
        route = TopLevelDestination.MOVIES.route,
    ) {
        composable("favorites/home") {
            FavoritesRoute(
                onNavigateToDetails = { id ->
                    navController.navigate("favorites/details/$id")
                },
                onNavigateToExpandedList = { type ->
                    navController.navigate("favorites/list/$type")
                }
            )
        }
        composable("favorites/details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            id?.let {
                FavoritesDetailsRoute(id = it)
            }
        }
    }
}