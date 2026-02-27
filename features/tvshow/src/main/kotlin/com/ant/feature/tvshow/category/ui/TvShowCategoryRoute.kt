package com.ant.feature.tvshow.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.tvshow.category.TvShowCategoryViewModel

@Composable
fun TvShowCategoryRoute(
    onNavigateToDetails: (tvShowId: Long) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TvShowCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TvShowCategoryScreen(
        uiState = uiState,
        onTvShowClick = onNavigateToDetails,
        onNavigateBack = onNavigateBack,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}
