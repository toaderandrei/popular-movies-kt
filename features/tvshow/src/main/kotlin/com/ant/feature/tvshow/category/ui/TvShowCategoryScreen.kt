package com.ant.feature.tvshow.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.tvshow.category.TvShowCategoryUiState
import com.ant.feature.tvshow.ui.components.TvShowCard
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowCategoryScreen(
    uiState: TvShowCategoryUiState,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryTitle(uiState.categoryType)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.tvShows.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.error)
                    }
                }

                uiState.tvShows.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No TV shows found")
                    }
                }

                else -> {
                    val gridState = rememberLazyGridState()

                    val shouldLoadMore = remember {
                        derivedStateOf {
                            val layoutInfo = gridState.layoutInfo
                            val lastVisibleIndex =
                                layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItems = layoutInfo.totalItemsCount
                            lastVisibleIndex >= totalItems - 6
                        }
                    }

                    LaunchedEffect(Unit) {
                        snapshotFlow { shouldLoadMore.value }
                            .distinctUntilChanged()
                            .filter { it }
                            .collect { onLoadMore() }
                    }

                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = uiState.tvShows, key = { it.id }) { tvShow ->
                            TvShowCard(
                                tvShow = tvShow,
                                onClick = { onTvShowClick(tvShow.id) },
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun categoryTitle(tvShowType: TvShowType): String {
    return when (tvShowType) {
        TvShowType.POPULAR -> "Popular TV Shows"
        TvShowType.TOP_RATED -> "Top Rated TV Shows"
        TvShowType.AIRING_TODAY -> "Airing Today"
        TvShowType.ONTV_NOW -> "On TV Now"
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowCategoryScreenContentPreview() {
    MaterialTheme {
        TvShowCategoryScreen(
            uiState = TvShowCategoryUiState(
                categoryType = TvShowType.POPULAR,
                tvShows = listOf(
                    TvShow(id = 1, name = "Breaking Bad", originalTitle = null, voteCount = null, overview = null, voteAverage = 9.5, backDropPath = null, posterPath = null, originalLanguage = "en"),
                    TvShow(id = 2, name = "Game of Thrones", originalTitle = null, voteCount = null, overview = null, voteAverage = 8.4, backDropPath = null, posterPath = null, originalLanguage = "en"),
                    TvShow(id = 3, name = "The Wire", originalTitle = null, voteCount = null, overview = null, voteAverage = 9.3, backDropPath = null, posterPath = null, originalLanguage = "en"),
                    TvShow(id = 4, name = "Stranger Things", originalTitle = null, voteCount = null, overview = null, voteAverage = 8.7, backDropPath = null, posterPath = null, originalLanguage = "en"),
                ),
                currentPage = 1,
                totalPages = 5,
            ),
            onTvShowClick = {},
            onNavigateBack = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowCategoryScreenLoadingPreview() {
    MaterialTheme {
        TvShowCategoryScreen(
            uiState = TvShowCategoryUiState(
                categoryType = TvShowType.TOP_RATED,
                isLoading = true,
            ),
            onTvShowClick = {},
            onNavigateBack = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowCategoryScreenLoadingMorePreview() {
    MaterialTheme {
        TvShowCategoryScreen(
            uiState = TvShowCategoryUiState(
                categoryType = TvShowType.POPULAR,
                tvShows = listOf(
                    TvShow(id = 1, name = "Breaking Bad", originalTitle = null, voteCount = null, overview = null, voteAverage = 9.5, backDropPath = null, posterPath = null, originalLanguage = "en"),
                    TvShow(id = 2, name = "Game of Thrones", originalTitle = null, voteCount = null, overview = null, voteAverage = 8.4, backDropPath = null, posterPath = null, originalLanguage = "en"),
                ),
                currentPage = 1,
                totalPages = 5,
                isLoadingMore = true,
            ),
            onTvShowClick = {},
            onNavigateBack = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowCategoryScreenErrorPreview() {
    MaterialTheme {
        TvShowCategoryScreen(
            uiState = TvShowCategoryUiState(
                categoryType = TvShowType.POPULAR,
                error = "Failed to load TV shows",
            ),
            onTvShowClick = {},
            onNavigateBack = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowCategoryScreenEmptyPreview() {
    MaterialTheme {
        TvShowCategoryScreen(
            uiState = TvShowCategoryUiState(
                categoryType = TvShowType.AIRING_TODAY,
                tvShows = emptyList(),
            ),
            onTvShowClick = {},
            onNavigateBack = {},
            onRefresh = {},
            onLoadMore = {},
        )
    }
}
