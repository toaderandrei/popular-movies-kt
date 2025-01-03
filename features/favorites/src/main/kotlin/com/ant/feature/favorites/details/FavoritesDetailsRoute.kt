package com.ant.feature.favorites.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesDetailsRoute(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: FavoritesDetailsViewModel = hiltViewModel(),
) {
    val movie = "Movie Title"

    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Text(text = "Movie Details", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Title: ${movie}")
        Text(text = "Description: ${movie}")
    }
}