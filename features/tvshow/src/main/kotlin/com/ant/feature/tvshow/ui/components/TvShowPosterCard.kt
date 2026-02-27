package com.ant.feature.tvshow.ui.components

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
import com.ant.models.entities.TvShow

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

/**
 * Simple TV show poster card for horizontal scrolling sections
 */
@Composable
fun TvShowPosterCard(
    tvShow: TvShow,
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
            model = tvShow.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
            contentDescription = tvShow.name,
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
private fun TvShowPosterCardPreview() {
    TvShowPosterCard(
        tvShow = TvShow(
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
        onClick = {}
    )
}
