package com.ant.feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ant.feature.MoviesRoute
import com.ant.feature.details.MovieDetailsRoute
import com.ant.ui.navigation.TopLevelDestination


fun NavController.navigateToMovies(navOptions: NavOptions) {}

fun NavGraphBuilder.moviesGraph(navController: NavController) {
    navigation(
        startDestination = "movies/home",
        route = TopLevelDestination.MOVIES.route,
    ) {
        composable("movies/home") {
            MoviesRoute(
                onNavigateToDetails = { id ->
                    navController.navigate("movies/details/$id")
                }
            )
        }
        composable("movies/details/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            id?.let {
                MovieDetailsRoute(id = id)
            }
        }
    }
}