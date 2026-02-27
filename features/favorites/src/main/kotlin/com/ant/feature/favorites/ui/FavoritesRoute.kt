package com.ant.feature.favorites.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.favorites.FavoritesViewModel

@Composable
fun FavoritesRoute(
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        FavoritesScreen(
            uiState = uiState,
            onMovieClick = onMovieClick,
            onTvShowClick = onTvShowClick,
            onTabChange = viewModel::onTabChange,
            onRefresh = viewModel::refresh,
            onSyncClick = viewModel::syncToRemote,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
