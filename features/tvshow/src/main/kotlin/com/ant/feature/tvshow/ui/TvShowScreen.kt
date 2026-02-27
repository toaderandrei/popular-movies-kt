package com.ant.feature.tvshow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.tvshow.TvShowSection
import com.ant.feature.tvshow.TvShowUiState
import com.ant.feature.tvshow.ui.components.TvShowSectionRow
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

/**
 * TV Shows screen - displays multiple sections of TV shows in vertical scroll
 * Each section has a horizontal scrolling row of posters
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowScreen(
    uiState: TvShowUiState,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onMoreClick: (TvShowType) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading && uiState.tvShowSections.isEmpty() -> {
                LoadingState(modifier = Modifier.fillMaxSize())
            }

            uiState.error != null && uiState.tvShowSections.isEmpty() -> {
                ErrorState(
                    error = uiState.error,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.tvShowSections.isEmpty() -> {
                EmptyState(modifier = Modifier.fillMaxSize())
            }

            else -> {
                TvShowSectionsList(
                    sections = uiState.tvShowSections,
                    onTvShowClick = onTvShowClick,
                    onMoreClick = onMoreClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun TvShowSectionsList(
    sections: Map<TvShowType, TvShowSection>,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onMoreClick: (TvShowType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val orderedCategories = listOf(
        TvShowType.POPULAR,
        TvShowType.TOP_RATED,
        TvShowType.AIRING_TODAY,
        TvShowType.ONTV_NOW
    )

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            items = orderedCategories.mapNotNull { category ->
                sections[category]?.let { category to it }
            },
            key = { (category, _) -> category }
        ) { (category, section) ->
            TvShowSectionRow(
                title = formatCategoryTitle(category),
                tvShows = section.tvShows,
                onTvShowClick = onTvShowClick,
                onMoreClick = { onMoreClick(category) },
                isLoading = section.isLoading,
                error = section.error
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

/**
 * Formats the category name for display
 */
private fun formatCategoryTitle(category: TvShowType): String {
    return when (category) {
        TvShowType.POPULAR -> "Popular"
        TvShowType.TOP_RATED -> "Top Rated"
        TvShowType.AIRING_TODAY -> "Airing Today"
        TvShowType.ONTV_NOW -> "On TV Now"
    }
}

@Preview
@Composable
private fun TvShowScreenPreview() {
    MaterialTheme {
        TvShowScreen(
            uiState = TvShowUiState(
                tvShowSections = mapOf(
                    TvShowType.POPULAR to TvShowSection(
                        category = TvShowType.POPULAR,
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
                        )
                    ),
                    TvShowType.TOP_RATED to TvShowSection(
                        category = TvShowType.TOP_RATED,
                        tvShows = listOf(
                            TvShow(
                                id = 3,
                                name = "The Wire",
                                originalTitle = null,
                                voteCount = null,
                                overview = null,
                                voteAverage = 8.8,
                                backDropPath = null,
                                posterPath = "/path/to/poster3.jpg",
                                originalLanguage = null
                            )
                        )
                    )
                )
            ),
            onTvShowClick = {},
            onMoreClick = {},
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
            onMoreClick = {},
            onRefresh = {}
        )
    }
}
