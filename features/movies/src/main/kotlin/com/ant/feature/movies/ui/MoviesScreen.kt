package com.ant.feature.movies.ui

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
import com.ant.feature.movies.MovieSection
import com.ant.feature.movies.MoviesUiState
import com.ant.feature.movies.ui.components.MovieSectionRow
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

/**
 * Movies screen - displays multiple sections of movies in vertical scroll
 * Each section has a horizontal scrolling row of posters
 * Matches the design from ui_app.png
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    uiState: MoviesUiState,
    onMovieClick: (movieId: Long) -> Unit,
    onMoreClick: (MovieType) -> Unit,
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
            uiState.isLoading && uiState.movieSections.isEmpty() -> {
                LoadingState(modifier = Modifier.fillMaxSize())
            }

            uiState.error != null && uiState.movieSections.isEmpty() -> {
                ErrorState(
                    error = uiState.error,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.movieSections.isEmpty() -> {
                EmptyState(modifier = Modifier.fillMaxSize())
            }

            else -> {
                MovieSectionsList(
                    sections = uiState.movieSections,
                    onMovieClick = onMovieClick,
                    onMoreClick = onMoreClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MovieSectionsList(
    sections: Map<MovieType, MovieSection>,
    onMovieClick: (movieId: Long) -> Unit,
    onMoreClick: (MovieType) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Define the order of sections to match the design: Popular, Top Rated, Now Playing, Upcoming
    val orderedCategories = listOf(
        MovieType.POPULAR,
        MovieType.TOP_RATED,
        MovieType.NOW_PLAYING,
        MovieType.UPCOMING
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
            MovieSectionRow(
                title = formatCategoryTitle(category),
                movies = section.movies,
                onMovieClick = onMovieClick,
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
            text = "No movies found",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Formats the category name for display
 */
private fun formatCategoryTitle(category: MovieType): String {
    return when (category) {
        MovieType.POPULAR -> "Popular"
        MovieType.TOP_RATED -> "Top Rated"
        MovieType.NOW_PLAYING -> "Now Playing"
        MovieType.UPCOMING -> "Upcoming"
    }
}

@Preview
@Composable
private fun MoviesScreenPreview() {
    MaterialTheme {
        MoviesScreen(
            uiState = MoviesUiState(
                movieSections = mapOf(
                    MovieType.POPULAR to MovieSection(
                        category = MovieType.POPULAR,
                        movies = listOf(
                            MovieData(
                                id = 1,
                                name = "Godzilla x Kong",
                                posterPath = "/path/to/poster1.jpg",
                                voteAverage = 7.5
                            ),
                            MovieData(
                                id = 2,
                                name = "Kingdom of the Planet of the Apes",
                                posterPath = "/path/to/poster2.jpg",
                                voteAverage = 7.2
                            )
                        )
                    ),
                    MovieType.TOP_RATED to MovieSection(
                        category = MovieType.TOP_RATED,
                        movies = listOf(
                            MovieData(
                                id = 3,
                                name = "The Shawshank Redemption",
                                posterPath = "/path/to/poster3.jpg",
                                voteAverage = 8.7
                            ),
                            MovieData(
                                id = 4,
                                name = "The Godfather",
                                posterPath = "/path/to/poster4.jpg",
                                voteAverage = 8.5
                            )
                        )
                    )
                )
            ),
            onMovieClick = {},
            onMoreClick = {},
            onRefresh = {}
        )
    }
}

@Preview
@Composable
private fun MoviesScreenLoadingPreview() {
    MaterialTheme {
        MoviesScreen(
            uiState = MoviesUiState(isLoading = true),
            onMovieClick = {},
            onMoreClick = {},
            onRefresh = {}
        )
    }
}
