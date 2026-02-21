package com.ant.feature.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.favorites.FavoriteTab
import com.ant.feature.favorites.FavoritesUiState
import com.ant.feature.favorites.ui.components.SyncStatusIcon
import com.ant.feature.movies.ui.components.MovieCard
import com.ant.feature.tvshow.ui.components.TvShowCard
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.request.FavoriteType
import com.ant.resources.R as R2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onTabChange: (FavoriteTab) -> Unit,
    onRefresh: () -> Unit,
    onSyncClick: (id: Long, mediaType: FavoriteType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            Tab(
                selected = uiState.selectedTab == FavoriteTab.MOVIES,
                onClick = { onTabChange(FavoriteTab.MOVIES) },
                text = { Text(stringResource(R2.string.movies)) }
            )
            Tab(
                selected = uiState.selectedTab == FavoriteTab.TV_SHOWS,
                onClick = { onTabChange(FavoriteTab.TV_SHOWS) },
                text = { Text(stringResource(R2.string.tvshow)) }
            )
        }

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
                                    message = stringResource(R2.string.empty_movie_results),
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
                                        items = uiState.favoriteMovies,
                                        key = { it.id }
                                    ) { movie ->
                                        MovieCard(
                                            movie = movie,
                                            onClick = { onMovieClick(movie.id) },
                                            trailingIcon = {
                                                SyncStatusIcon(
                                                    isSynced = movie.syncedToRemote == true,
                                                    isSyncing = movie.id in uiState.syncingIds,
                                                    onSyncClick = {
                                                        onSyncClick(movie.id, FavoriteType.MOVIE)
                                                    },
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        FavoriteTab.TV_SHOWS -> {
                            if (uiState.favoriteTvShows.isEmpty()) {
                                EmptyState(
                                    message = stringResource(R2.string.empty_results),
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
                                        items = uiState.favoriteTvShows,
                                        key = { it.id }
                                    ) { tvShow ->
                                        TvShowCard(
                                            tvShow = tvShow,
                                            onClick = { onTvShowClick(tvShow.id) },
                                            trailingIcon = {
                                                SyncStatusIcon(
                                                    isSynced = tvShow.syncedToRemote == true,
                                                    isSyncing = tvShow.id in uiState.syncingIds,
                                                    onSyncClick = {
                                                        onSyncClick(tvShow.id, FavoriteType.TV)
                                                    },
                                                )
                                            },
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

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenContentPreview() {
    MaterialTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                favoriteMovies = listOf(
                    MovieData(id = 1, name = "The Shawshank Redemption"),
                    MovieData(id = 2, name = "The Godfather", syncedToRemote = true),
                ),
                selectedTab = FavoriteTab.MOVIES,
            ),
            onMovieClick = {},
            onTvShowClick = {},
            onTabChange = {},
            onRefresh = {},
            onSyncClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenLoadingPreview() {
    MaterialTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(isLoading = true),
            onMovieClick = {},
            onTvShowClick = {},
            onTabChange = {},
            onRefresh = {},
            onSyncClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenErrorPreview() {
    MaterialTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(error = "Failed to load favorites"),
            onMovieClick = {},
            onTvShowClick = {},
            onTabChange = {},
            onRefresh = {},
            onSyncClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenEmptyPreview() {
    MaterialTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                favoriteMovies = emptyList(),
                selectedTab = FavoriteTab.MOVIES,
            ),
            onMovieClick = {},
            onTvShowClick = {},
            onTabChange = {},
            onRefresh = {},
            onSyncClick = { _, _ -> },
        )
    }
}
