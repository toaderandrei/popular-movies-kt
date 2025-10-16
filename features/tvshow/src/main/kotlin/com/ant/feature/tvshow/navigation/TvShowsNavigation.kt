package com.ant.feature.tvshow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ant.feature.tvshow.ui.TvShowRoute

const val TV_SHOW_ROUTE = "tvshow"

/**
 * Navigate to the TV Show screen
 */
fun NavController.navigateToTvShow(navOptions: NavOptions? = null) {
    navigate(TV_SHOW_ROUTE, navOptions)
}

/**
 * Add TV Show screen to the navigation graph
 */
fun NavGraphBuilder.tvShowScreen(
    onNavigateToDetails: (tvShowId: Long) -> Unit,
) {
    composable(route = TV_SHOW_ROUTE) {
        TvShowRoute(onNavigateToDetails = onNavigateToDetails)
    }
}