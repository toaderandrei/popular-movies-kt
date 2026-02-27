package com.ant.feature.tvshow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ant.feature.tvshow.details.TvShowDetailsRoute
import com.ant.feature.tvshow.ui.TvShowRoute
import com.ant.models.request.TvShowType

const val TV_SHOW_ROUTE = "tvshow"
const val TV_SHOW_DETAILS_ROUTE = "tvshow/details/{tvShowId}"
const val TV_SHOW_CATEGORY_ROUTE = "tvshow/category/{categoryType}"

/**
 * Navigate to the TV Show screen
 */
fun NavController.navigateToTvShow(navOptions: NavOptions? = null) {
    navigate(TV_SHOW_ROUTE, navOptions)
}

fun NavController.navigateToTvShowDetails(tvShowId: Long) {
    navigate("tvshow/details/$tvShowId")
}

fun NavController.navigateToTvShowCategory(categoryType: TvShowType) {
    navigate("tvshow/category/${categoryType.name}")
}

/**
 * Add TV Show screen to the navigation graph
 */
fun NavGraphBuilder.tvShowScreen(
    onNavigateToDetails: (tvShowId: Long) -> Unit,
    onNavigateToCategory: (TvShowType) -> Unit = {},
) {
    composable(route = TV_SHOW_ROUTE) {
        TvShowRoute(
            onNavigateToDetails = onNavigateToDetails,
            onNavigateToCategory = onNavigateToCategory,
        )
    }
}

fun NavGraphBuilder.tvShowDetailsScreen(
    onNavigateBack: () -> Unit,
) {
    composable(
        route = TV_SHOW_DETAILS_ROUTE,
        arguments = listOf(
            navArgument("tvShowId") { type = NavType.LongType }
        )
    ) {
        TvShowDetailsRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.tvShowCategoryScreen(
    onNavigateToDetails: (tvShowId: Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = TV_SHOW_CATEGORY_ROUTE,
        arguments = listOf(
            navArgument("categoryType") { type = NavType.StringType }
        )
    ) {
        com.ant.feature.tvshow.category.ui.TvShowCategoryRoute(
            onNavigateToDetails = onNavigateToDetails,
            onNavigateBack = onNavigateBack,
        )
    }
}
