package com.ant.feature.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.search.SearchViewModel

@Composable
fun SearchRoute(
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onTabChange = viewModel::onTabChange,
        onMovieClick = onMovieClick,
        onTvShowClick = onTvShowClick,
        onClearSearch = viewModel::clearSearch,
        modifier = modifier,
    )
}
