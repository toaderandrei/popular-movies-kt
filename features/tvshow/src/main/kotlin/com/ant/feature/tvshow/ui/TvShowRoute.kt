package com.ant.feature.tvshow.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.tvshow.TvShowViewModel

/**
 * Route composable for TV Show screen - handles ViewModel injection and state collection
 */
@Composable
fun TvShowRoute(
    onNavigateToDetails: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TvShowViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TvShowScreen(
        uiState = uiState,
        onTvShowClick = onNavigateToDetails,
        onCategoryChange = viewModel::onCategoryChange,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}
