package com.ant.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.ant.resources.R as R2

object Graph {
    const val ROOT = "root"
    const val AUTHENTICATION = "account"
    const val MAIN  = "main"
}

enum class MainScreenDestination(
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
    );
}

enum class LoginScreenDestination(
    val route: String,
    val titleTextId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    LOGIN(
        route = "login",
        titleTextId = R2.string.login,
        selectedIcon = Icons.AutoMirrored.Filled.Login,
        unselectedIcon = Icons.AutoMirrored.Outlined.Login
    );
}