package com.ant.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchRoute(
    onNavigateToDetails: (String, String) -> Unit,
    onNavigateToExpandedList: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = "",
            onValueChange = {
                // handle search
            },
            modifier = Modifier.fillMaxSize(),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                )
            },
            placeholder = { Text("Search for movies or tv shows") }
        )
        Text(
            "Search results...",
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyColumn {
            items(5) { index ->
                val itemType = if (index % 2 == 0) "movie" else "tvshow"
                val itemId = (index + 1).toString()
                Text(
                    "Dummy result: $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            onNavigateToDetails(itemId, itemType)
                        }
                )
            }
        }
    }
}