package com.ant.feature.movies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.movies.ui.MoviesRoute
import com.ant.models.request.MovieType

const val MOVIES_ROUTE = "movies"

/**
 * Navigate to the Movies screen
 */
fun NavController.navigateToMovies(navOptions: NavOptions? = null) {
    navigate(MOVIES_ROUTE, navOptions)
}

/**
 * Add Movies screen to the navigation graph
 */
fun NavGraphBuilder.moviesScreen(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateToCategory: (MovieType) -> Unit = {},
) {
    composable(route = MOVIES_ROUTE) {
        MoviesRoute(
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToCategory = onNavigateToCategory,
        )
    }
}
