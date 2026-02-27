package com.ant.feature.movies.details

import com.ant.models.entities.MovieDetails

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieDetails? = null,
    val error: String? = null,
    val isFavorite: Boolean = false,
)
