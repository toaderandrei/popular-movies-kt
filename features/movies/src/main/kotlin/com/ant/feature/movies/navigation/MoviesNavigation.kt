package com.ant.feature.movies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ant.feature.movies.details.MovieDetailsRoute
import com.ant.feature.movies.ui.MoviesRoute
import com.ant.models.request.MovieType

const val MOVIES_ROUTE = "movies"
const val MOVIE_DETAILS_ROUTE = "movies/details/{movieId}"
const val MOVIE_CATEGORY_ROUTE = "movies/category/{categoryType}"

/**
 * Navigate to the Movies screen
 */
fun NavController.navigateToMovies(navOptions: NavOptions? = null) {
    navigate(MOVIES_ROUTE, navOptions)
}

fun NavController.navigateToMovieDetails(movieId: Long) {
    navigate("movies/details/$movieId")
}

fun NavController.navigateToMovieCategory(categoryType: MovieType) {
    navigate("movies/category/${categoryType.name}")
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

fun NavGraphBuilder.movieDetailsScreen(
    onNavigateBack: () -> Unit,
) {
    composable(
        route = MOVIE_DETAILS_ROUTE,
        arguments = listOf(
            navArgument("movieId") { type = NavType.LongType }
        )
    ) {
        MovieDetailsRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.movieCategoryScreen(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = MOVIE_CATEGORY_ROUTE,
        arguments = listOf(
            navArgument("categoryType") { type = NavType.StringType }
        )
    ) {
        com.ant.feature.movies.category.ui.MovieCategoryRoute(
            onNavigateToDetails = onNavigateToDetails,
            onNavigateBack = onNavigateBack,
        )
    }
}
