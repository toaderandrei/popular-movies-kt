package com.ant.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text

@Composable
internal fun MovieDetailsRoute(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    // UI for movies home screen
    Text(
        text = "Movies Details Screen",
        modifier = modifier
    )
}
