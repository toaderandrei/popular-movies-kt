package com.ant.feature.movies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ant.models.entities.MovieData

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

/**
 * Simple movie poster card for horizontal scrolling sections
 * Matches the design from ui_app.png
 */
@Composable
fun MoviePosterCard(
    movie: MovieData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = movie.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
            contentDescription = movie.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun MoviePosterCardPreview() {
    MoviePosterCard(
        movie = MovieData(
            id = 1,
            name = "The Shawshank Redemption",
            posterPath = "/path/to/poster.jpg",
            voteAverage = 8.7
        ),
        onClick = {}
    )
}
