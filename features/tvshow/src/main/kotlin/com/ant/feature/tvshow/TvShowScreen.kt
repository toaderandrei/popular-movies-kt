package com.ant.feature.tvshow

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
fun TvShowRoute(
    onNavigateToDetails: (String) -> Unit,
    onNavigateToExpandedList: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TvShowViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "TvShow",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(onClick = { onNavigateToExpandedList("popular") }) {
            Text("View Popular TvShows")
        }
        Button(onClick = { onNavigateToExpandedList("top") }) {
            Text("View Top TvShows")
        }
    }
}