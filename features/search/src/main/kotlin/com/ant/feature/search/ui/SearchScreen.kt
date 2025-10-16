package com.ant.feature.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.search.SearchTab
import com.ant.feature.search.SearchUiState
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

/**
 * Search screen with search bar and tabbed results for Movies and TV Shows
 */
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onTabChange: (SearchTab) -> Unit,
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Search bar
        TextField(
            value = uiState.query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search for movies or TV shows") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        if (uiState.query.isNotEmpty()) {
            // Tabs
            TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
                Tab(
                    selected = uiState.selectedTab == SearchTab.MOVIES,
                    onClick = { onTabChange(SearchTab.MOVIES) },
                    text = { Text("Movies (${uiState.movieResults.size})") }
                )
                Tab(
                    selected = uiState.selectedTab == SearchTab.TV_SHOWS,
                    onClick = { onTabChange(SearchTab.TV_SHOWS) },
                    text = { Text("TV Shows (${uiState.tvShowResults.size})") }
                )
            }

            // Content
            when {
                uiState.isSearching -> {
                    LoadingState(modifier = Modifier.fillMaxSize())
                }

                uiState.error != null -> {
                    ErrorState(
                        error = uiState.error,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    when (uiState.selectedTab) {
                        SearchTab.MOVIES -> {
                            if (uiState.movieResults.isEmpty()) {
                                EmptyState(
                                    message = "No movies found for \"${uiState.query}\"",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        count = uiState.movieResults.size,
                                        key = { uiState.movieResults[it].id }
                                    ) { index ->
                                        val movie = uiState.movieResults[index]
                                        com.ant.feature.movies.ui.components.MovieCard(
                                            movie = movie,
                                            onClick = { onMovieClick(movie.id) }
                                        )
                                    }
                                }
                            }
                        }

                        SearchTab.TV_SHOWS -> {
                            if (uiState.tvShowResults.isEmpty()) {
                                EmptyState(
                                    message = "No TV shows found for \"${uiState.query}\"",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        count = uiState.tvShowResults.size,
                                        key = { uiState.tvShowResults[it].id }
                                    ) { index ->
                                        val tvShow = uiState.tvShowResults[index]
                                        com.ant.feature.tvshow.ui.components.TvShowCard(
                                            tvShow = tvShow,
                                            onClick = { onTvShowClick(tvShow.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Empty state - show prompt
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search for your favorite movies and TV shows",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    error: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "Inception",
                movieResults = listOf(
                    MovieData(id = 1, name = "Inception"),
                    MovieData(id = 2, name = "Interstellar")
                ),
                selectedTab = SearchTab.MOVIES
            ),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {}
        )
    }
}
