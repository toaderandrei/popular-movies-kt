package com.ant.feature.movies.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ant.feature.movies.MoviesRoute
import com.ant.feature.movies.details.MovieDetailsRoute
import com.ant.ui.navigation.MainScreenDestination

fun NavGraphBuilder.moviesNavigation(navController: NavHostController) {
    composable(MainScreenDestination.MOVIES.route) {
        MoviesRoute(
            onClick = {}
        )
    }
    composable("movies/details/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        id?.let {
            MovieDetailsRoute(id = it)
        }
    }
}