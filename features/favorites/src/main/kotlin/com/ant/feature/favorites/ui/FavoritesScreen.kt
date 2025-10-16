package com.ant.feature.favorites.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.favorites.FavoriteTab
import com.ant.feature.favorites.FavoritesUiState
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

/**
 * Favorites screen with tabs for Movies and TV Shows
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onTabChange: (FavoriteTab) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Tabs
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            Tab(
                selected = uiState.selectedTab == FavoriteTab.MOVIES,
                onClick = { onTabChange(FavoriteTab.MOVIES) },
                text = { Text("Movies") }
            )
            Tab(
                selected = uiState.selectedTab == FavoriteTab.TV_SHOWS,
                onClick = { onTabChange(FavoriteTab.TV_SHOWS) },
                text = { Text("TV Shows") }
            )
        }

        // Content
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
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
                        FavoriteTab.MOVIES -> {
                            if (uiState.favoriteMovies.isEmpty()) {
                                EmptyState(
                                    message = "No favorite movies yet",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Reuse MovieCard from movies feature
                                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        count = uiState.favoriteMovies.size,
                                        key = { uiState.favoriteMovies[it].id }
                                    ) { index ->
                                        val movie = uiState.favoriteMovies[index]
                                        com.ant.feature.movies.ui.components.MovieCard(
                                            movie = movie,
                                            onClick = { onMovieClick(movie.id) }
                                        )
                                    }
                                }
                            }
                        }

                        FavoriteTab.TV_SHOWS -> {
                            if (uiState.favoriteTvShows.isEmpty()) {
                                EmptyState(
                                    message = "No favorite TV shows yet",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Reuse TvShowCard from tvshow feature
                                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        count = uiState.favoriteTvShows.size,
                                        key = { uiState.favoriteTvShows[it].id }
                                    ) { index ->
                                        val tvShow = uiState.favoriteTvShows[index]
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
private fun FavoritesScreenPreview() {
    MaterialTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                favoriteMovies = listOf(
                    MovieData(id = 1, name = "The Shawshank Redemption"),
                    MovieData(id = 2, name = "The Godfather")
                ),
                selectedTab = FavoriteTab.MOVIES
            ),
            onMovieClick = {},
            onTvShowClick = {},
            onTabChange = {},
            onRefresh = {}
        )
    }
}
