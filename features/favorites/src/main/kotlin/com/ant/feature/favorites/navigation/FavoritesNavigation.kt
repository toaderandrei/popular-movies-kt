package com.ant.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.favorites.ui.FavoritesRoute

const val FAVORITES_ROUTE = "favorites"

/**
 * Navigate to the Favorites screen
 */
fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
    navigate(FAVORITES_ROUTE, navOptions)
}

/**
 * Add Favorites screen to the navigation graph
 */
fun NavGraphBuilder.favoritesScreen(
    onNavigateToMovieDetails: (movieId: Long) -> Unit,
    onNavigateToTvShowDetails: (tvShowId: Long) -> Unit,
) {
    composable(route = FAVORITES_ROUTE) {
        FavoritesRoute(
            onMovieClick = onNavigateToMovieDetails,
            onTvShowClick = onNavigateToTvShowDetails,
        )
    }
}
