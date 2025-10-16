package com.ant.feature.tvshow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.tvshow.TvShowUiState
import com.ant.feature.tvshow.ui.components.TvShowCard
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

/**
 * TV Shows screen - displays a grid of TV shows with category tabs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowScreen(
    uiState: TvShowUiState,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onCategoryChange: (TvShowType) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Category tabs
        CategoryTabs(
            selectedCategory = uiState.selectedCategory,
            onCategoryChange = onCategoryChange,
            modifier = Modifier.fillMaxWidth()
        )

        // Content
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.isLoading && uiState.tvShows.isEmpty() -> {
                    LoadingState(modifier = Modifier.fillMaxSize())
                }

                uiState.error != null && uiState.tvShows.isEmpty() -> {
                    ErrorState(
                        error = uiState.error,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                uiState.tvShows.isEmpty() -> {
                    EmptyState(modifier = Modifier.fillMaxSize())
                }

                else -> {
                    TvShowsGrid(
                        tvShows = uiState.tvShows,
                        onTvShowClick = onTvShowClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTabs(
    selectedCategory: TvShowType,
    onCategoryChange: (TvShowType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categories = TvShowType.entries.toList()

    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        modifier = modifier,
        edgePadding = 16.dp
    ) {
        categories.forEach { category ->
            Tab(
                selected = category == selectedCategory,
                onClick = { onCategoryChange(category) },
                text = {
                    Text(
                        text = category.name.replace("_", " "),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
private fun TvShowsGrid(
    tvShows: List<TvShow>,
    onTvShowClick: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(
            items = tvShows,
            key = { it.id }
        ) { tvShow ->
            TvShowCard(
                tvShow = tvShow,
                onClick = { onTvShowClick(tvShow.id) }
            )
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
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No TV shows found",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun TvShowScreenPreview() {
    MaterialTheme {
        TvShowScreen(
            uiState = TvShowUiState(
                tvShows = listOf(
                    TvShow(
                        id = 1,
                        name = "Breaking Bad",
                        originalTitle = null,
                        voteCount = null,
                        overview = null,
                        voteAverage = 9.5,
                        backDropPath = null,
                        posterPath = "/path/to/poster.jpg",
                        originalLanguage = null
                    ),
                    TvShow(
                        id = 2,
                        name = "Game of Thrones",
                        originalTitle = null,
                        voteCount = null,
                        overview = null,
                        voteAverage = 9.3,
                        backDropPath = null,
                        posterPath = "/path/to/poster2.jpg",
                        originalLanguage = null
                    )
                ),
                selectedCategory = TvShowType.POPULAR
            ),
            onTvShowClick = {},
            onCategoryChange = {},
            onRefresh = {}
        )
    }
}

@Preview
@Composable
private fun TvShowScreenLoadingPreview() {
    MaterialTheme {
        TvShowScreen(
            uiState = TvShowUiState(isLoading = true),
            onTvShowClick = {},
            onCategoryChange = {},
            onRefresh = {}
        )
    }
}
