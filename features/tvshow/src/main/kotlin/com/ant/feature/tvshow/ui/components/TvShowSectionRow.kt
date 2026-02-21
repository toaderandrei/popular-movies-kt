package com.ant.feature.tvshow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

/**
 * A horizontal scrolling row of TV shows with a section header
 */
@Composable
fun TvShowSectionRow(
    title: String,
    tvShows: List<TvShow>,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        SectionHeader(
            title = title,
            onMoreClick = onMoreClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        when {
            isLoading -> {
                LoadingState(modifier = Modifier.fillMaxWidth())
            }

            error != null -> {
                ErrorState(
                    error = error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            tvShows.isEmpty() -> {
                EmptyState(modifier = Modifier.fillMaxWidth())
            }

            else -> {
                TvShowRow(
                    tvShows = tvShows,
                    onTvShowClick = onTvShowClick
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextButton(onClick = onMoreClick) {
            Text(
                text = "MORE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TvShowRow(
    tvShows: List<TvShow>,
    onTvShowClick: (tvShowId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = tvShows,
            key = { it.id }
        ) { tvShow ->
            TvShowPosterCard(
                tvShow = tvShow,
                onClick = { onTvShowClick(tvShow.id) },
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(210.dp)
            .padding(16.dp),
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
        modifier = modifier
            .height(210.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(210.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No TV shows found",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun TvShowSectionRowPreview() {
    MaterialTheme {
        TvShowSectionRow(
            title = "Popular",
            tvShows = listOf(
                TvShow(
                    id = 1,
                    name = "Breaking Bad",
                    originalTitle = null,
                    voteCount = null,
                    overview = null,
                    voteAverage = 9.5,
                    backDropPath = null,
                    posterPath = "/path/to/poster1.jpg",
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
                ),
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
            ),
            onTvShowClick = {},
            onMoreClick = {}
        )
    }
}
