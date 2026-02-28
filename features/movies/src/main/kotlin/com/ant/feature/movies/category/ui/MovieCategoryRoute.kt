package com.ant.feature.movies.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.movies.category.MovieCategoryViewModel

@Composable
fun MovieCategoryRoute(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieCategoryScreen(
        uiState = uiState,
        onMovieClick = onNavigateToDetails,
        onNavigateBack = onNavigateBack,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadNextPage,
        modifier = modifier,
    )
}
