package com.ant.feature.movies.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieDetails
import com.ant.models.entities.MovieVideo

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val TMDB_BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w1280"
private val HEADER_HEIGHT = 260.dp

@Composable
fun MovieDetailsRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailsScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onToggleFavorite = viewModel::toggleFavorite,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    uiState: MovieDetailsUiState,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = uiState.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }

        uiState.movieDetails != null -> {
            CollapsingHeaderContent(
                movieDetails = uiState.movieDetails,
                isFavorite = uiState.isFavorite,
                onNavigateBack = onNavigateBack,
                onToggleFavorite = onToggleFavorite,
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsingHeaderContent(
    movieDetails: MovieDetails,
    isFavorite: Boolean,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val movie = movieDetails.movieData
    val scrollState = rememberScrollState()
    val headerHeightPx = with(LocalDensity.current) { HEADER_HEIGHT.toPx() }

    // 0f = fully expanded, 1f = fully collapsed
    val collapseFraction = (scrollState.value / headerHeightPx).coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Collapsing backdrop image with parallax
        movie.backDropPath?.let { backdropPath ->
            AsyncImage(
                model = "$TMDB_BACKDROP_BASE_URL$backdropPath",
                contentDescription = movie.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HEADER_HEIGHT)
                    .graphicsLayer {
                        // Parallax: image scrolls at half speed
                        translationY = -scrollState.value * 0.5f
                        alpha = 1f - collapseFraction
                    },
                contentScale = ContentScale.Crop,
            )
        }

        // 2. Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            // Spacer to sit below the backdrop
            Spacer(modifier = Modifier.height(HEADER_HEIGHT))

            // Scrim gradient at the bottom of the image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface,
                            ),
                        ),
                    ),
            )

            // Content on surface background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                // Info row: poster + details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    AsyncImage(
                        model = movie.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
                        contentDescription = movie.name,
                        modifier = Modifier
                            .width(100.dp)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = movie.name ?: "Unknown Title",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        movie.releaseDate?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        movie.runtime?.let {
                            Text(
                                text = "${it} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        movie.genres?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        movie.voteAverage?.let { rating ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%.1f", rating),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                // Overview
                movie.overview?.let { overview ->
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                // Cast section
                movieDetails.movieCasts?.takeIf { it.isNotEmpty() }?.let { casts ->
                    Spacer(modifier = Modifier.height(20.dp))
                    CastSection(casts = casts)
                }

                // Videos section
                movieDetails.videos?.takeIf { it.isNotEmpty() }?.let { videos ->
                    Spacer(modifier = Modifier.height(20.dp))
                    VideosSection(videos = videos)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // 3. Floating top app bar: transparent → opaque as you scroll
        TopAppBar(
            title = {
                Text(
                    text = movie.name ?: "Movie Details",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer { alpha = collapseFraction },
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (collapseFraction < 0.5f) Color.White
                        else MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            actions = {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites"
                        else "Add to favorites",
                        tint = if (isFavorite) Color.Red
                        else if (collapseFraction < 0.5f) Color.White
                        else MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(
                    alpha = collapseFraction,
                ),
            ),
            modifier = Modifier.statusBarsPadding(),
        )
    }
}

@Composable
private fun CastSection(
    casts: List<MovieCast>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(
                items = casts,
                key = { index, cast -> cast.creditId ?: "cast_$index" },
            ) { _, cast ->
                CastItem(cast = cast)
            }
        }
    }
}

@Composable
private fun CastItem(
    cast: MovieCast,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(80.dp),
    ) {
        AsyncImage(
            model = cast.profileImagePath?.let { "$TMDB_IMAGE_BASE_URL$it" },
            contentDescription = cast.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cast.name ?: "",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun VideosSection(
    videos: List<MovieVideo>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Videos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(
                items = videos,
                key = { index, video -> video.key ?: "video_$index" },
            ) { _, video ->
                VideoItem(video = video)
            }
        }
    }
}

@Composable
private fun VideoItem(
    video: MovieVideo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(200.dp),
    ) {
        AsyncImage(
            model = video.key?.let { "https://img.youtube.com/vi/$it/mqdefault.jpg" },
            contentDescription = video.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = video.name ?: "",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenLoadingPreview() {
    MaterialTheme {
        MovieDetailsScreen(
            uiState = MovieDetailsUiState(isLoading = true),
            onNavigateBack = {},
            onToggleFavorite = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenContentPreview() {
    MaterialTheme {
        MovieDetailsScreen(
            uiState = MovieDetailsUiState(
                isLoading = false,
                movieDetails = MovieDetails(
                    movieData = MovieData(
                        id = 1,
                        name = "The Shawshank Redemption",
                        overview = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        voteAverage = 8.7,
                        posterPath = null,
                        backDropPath = null,
                        runtime = "142",
                        _genres = listOf("Drama", "Crime"),
                    ),
                    movieCasts = listOf(
                        MovieCast(id = 1, creditId = 1, name = "Tim Robbins"),
                        MovieCast(id = 2, creditId = 2, name = "Morgan Freeman"),
                    ),
                    videos = listOf(
                        MovieVideo(id = 1, key = "abc", name = "Official Trailer", iso_639_1 = null, iso_3166_1 = null, site = "YouTube", size = 1080, type = null),
                    ),
                ),
                isFavorite = true,
            ),
            onNavigateBack = {},
            onToggleFavorite = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenErrorPreview() {
    MaterialTheme {
        MovieDetailsScreen(
            uiState = MovieDetailsUiState(
                isLoading = false,
                error = "Failed to load movie details",
            ),
            onNavigateBack = {},
            onToggleFavorite = {},
            onRetry = {},
        )
    }
}
