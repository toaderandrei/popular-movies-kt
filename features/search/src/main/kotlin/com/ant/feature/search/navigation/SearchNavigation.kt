package com.ant.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.search.ui.SearchRoute

const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    navigate(SEARCH_ROUTE, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onNavigateToMovieDetails: (movieId: Long) -> Unit,
    onNavigateToTvShowDetails: (tvShowId: Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(route = SEARCH_ROUTE) {
        SearchRoute(
            onMovieClick = onNavigateToMovieDetails,
            onTvShowClick = onNavigateToTvShowDetails,
            onNavigateBack = onNavigateBack,
        )
    }
}
