package com.ant.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text

@Composable
internal fun MoviesRoute(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    // UI for movies home screen
    Text(
        text = "Movies Home Screen",
        modifier = modifier
    )
}
