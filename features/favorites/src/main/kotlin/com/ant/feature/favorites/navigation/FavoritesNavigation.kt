package com.ant.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ant.feature.favorites.FavoritesRoute
import com.ant.feature.favorites.details.FavoritesDetailsRoute
import com.ant.ui.navigation.Graph
import com.ant.ui.navigation.MainScreenDestination


fun NavController.navigateToMovies(navOptions: NavOptions) {}

fun NavGraphBuilder.favoritesGraph(navController: NavController) {
//    navigation(
//        route = "${Graph.MAIN}/${MainScreenDestination.FAVORITES.route}",
//        startDestination = "${MainScreenDestination.FAVORITES.route}/home", // Use TopLevelDestination
//    ) {
//        composable("${MainScreenDestination.FAVORITES.route}/home") {
//            FavoritesRoute(
//                onNavigateToDetails = { id ->
//                    navController.navigate("${MainScreenDestination.FAVORITES.route}/details/$id")
//                },
//                onNavigateToExpandedList = { type ->
//                    navController.navigate("${MainScreenDestination.FAVORITES.route}/list/$type")
//                }
//            )
//        }
//        composable("${MainScreenDestination.FAVORITES.route}/details/{id}") { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("id")
//            id?.let {
//                FavoritesDetailsRoute(id = it)
//            }
//        }
//    }
}