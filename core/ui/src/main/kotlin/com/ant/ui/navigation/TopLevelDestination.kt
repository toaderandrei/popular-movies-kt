package com.ant.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.ant.resources.R as R2

enum class TopLevelDestination(
    val route: String,
    val titleTextId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    MOVIES(
        route = "movies",
        titleTextId = R2.string.movies,
        selectedIcon = Icons.Filled.Movie,
        unselectedIcon = Icons.Outlined.Movie
    ),
    TV_SHOW(
        route = "tv_show",
        titleTextId = R2.string.tvshow,
        selectedIcon = Icons.Filled.Tv,
        unselectedIcon = Icons.Outlined.Tv
    ),
    FAVORITES(
        route = "favorites",
        titleTextId = R2.string.favourites,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.Favorite
    );
}

object Graph {
    const val ROOT = "root"
    const val account = "account"
    const val MOVIES = "movies"
    const val TVSHOW = "tvshow"
}