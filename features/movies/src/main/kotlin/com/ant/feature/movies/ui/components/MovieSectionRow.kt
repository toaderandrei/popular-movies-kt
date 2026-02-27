package com.ant.feature.movies.ui.components

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
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

/**
 * A horizontal scrolling row of movies with a section header
 */
@Composable
fun MovieSectionRow(
    title: String,
    movies: List<MovieData>,
    onMovieClick: (movieId: Long) -> Unit,
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

            movies.isEmpty() -> {
                EmptyState(modifier = Modifier.fillMaxWidth())
            }

            else -> {
                MovieRow(
                    movies = movies,
                    onMovieClick = onMovieClick
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
private fun MovieRow(
    movies: List<MovieData>,
    onMovieClick: (movieId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = movies,
            key = { it.id }
        ) { movie ->
            MoviePosterCard(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
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
            text = "No movies found",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun MovieSectionRowPreview() {
    MaterialTheme {
        MovieSectionRow(
            title = "Popular",
            movies = listOf(
                MovieData(
                    id = 1,
                    name = "Godzilla x Kong: The New Empire",
                    posterPath = "/path/to/poster1.jpg",
                    voteAverage = 7.5
                ),
                MovieData(
                    id = 2,
                    name = "Kingdom of the Planet of the Apes",
                    posterPath = "/path/to/poster2.jpg",
                    voteAverage = 7.2
                ),
                MovieData(
                    id = 3,
                    name = "The Shawshank Redemption",
                    posterPath = "/path/to/poster3.jpg",
                    voteAverage = 8.7
                )
            ),
            onMovieClick = {},
            onMoreClick = {}
        )
    }
}
