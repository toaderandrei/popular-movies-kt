package com.ant.feature.favorites.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.favorites.FavoritesViewModel

/**
 * Route composable for Favorites screen
 */
@Composable
fun FavoritesRoute(
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onTvShowClick = onTvShowClick,
        onTabChange = viewModel::onTabChange,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}
