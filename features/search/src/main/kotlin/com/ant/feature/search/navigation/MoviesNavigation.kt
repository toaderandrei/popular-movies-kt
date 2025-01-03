package com.ant.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ant.feature.search.SearchRoute


fun NavController.navigateToMovies(navOptions: NavOptions) {}

fun NavGraphBuilder.searchGraph(navController: NavController) {
    navigation(
        startDestination = "search/home",
        route = "Search",
    ) {
        composable("search/home") {
            SearchRoute(
                onNavigateToDetails = { id, type ->
                    when (type) {
                        "movie" -> navController.navigate("movies/details/$id")
                        "tvshow" -> navController.navigate("tvseries/details/$id")
                    }
                },
                onNavigateToExpandedList = {
                    // TODO
                }
            )
        }
    }
}