package com.ant.feature.movies.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.feature.movies.category.MovieCategoryUiState
import com.ant.feature.movies.ui.components.MovieCard
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCategoryScreen(
    uiState: MovieCategoryUiState,
    onMovieClick: (movieId: Long) -> Unit,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
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

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.error)
                    }
                }

                uiState.movies.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No movies found")
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = uiState.movies, key = { it.id }) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun categoryTitle(movieType: MovieType): String {
    return when (movieType) {
        MovieType.POPULAR -> "Popular Movies"
        MovieType.TOP_RATED -> "Top Rated Movies"
        MovieType.NOW_PLAYING -> "Now Playing"
        MovieType.UPCOMING -> "Upcoming Movies"
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCategoryScreenContentPreview() {
    MaterialTheme {
        MovieCategoryScreen(
            uiState = MovieCategoryUiState(
                categoryType = MovieType.POPULAR,
                movies = listOf(
                    MovieData(id = 1, name = "The Shawshank Redemption"),
                    MovieData(id = 2, name = "The Godfather"),
                    MovieData(id = 3, name = "The Dark Knight"),
                    MovieData(id = 4, name = "Inception"),
                ),
            ),
            onMovieClick = {},
            onNavigateBack = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCategoryScreenLoadingPreview() {
    MaterialTheme {
        MovieCategoryScreen(
            uiState = MovieCategoryUiState(
                categoryType = MovieType.TOP_RATED,
                isLoading = true,
            ),
            onMovieClick = {},
            onNavigateBack = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCategoryScreenErrorPreview() {
    MaterialTheme {
        MovieCategoryScreen(
            uiState = MovieCategoryUiState(
                categoryType = MovieType.POPULAR,
                error = "Failed to load movies",
            ),
            onMovieClick = {},
            onNavigateBack = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCategoryScreenEmptyPreview() {
    MaterialTheme {
        MovieCategoryScreen(
            uiState = MovieCategoryUiState(
                categoryType = MovieType.UPCOMING,
                movies = emptyList(),
            ),
            onMovieClick = {},
            onNavigateBack = {},
            onRefresh = {},
        )
    }
}
