package com.ant.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoritesRoute(
    onNavigateToDetails: (String) -> Unit,
    onNavigateToExpandedList: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(onClick = { onNavigateToExpandedList("movies") }) {
            Text("View Movie favorite")
        }
        Button(onClick = { onNavigateToExpandedList("tvshow") }) {
            Text("View Favorites tv shows")
        }
    }
}