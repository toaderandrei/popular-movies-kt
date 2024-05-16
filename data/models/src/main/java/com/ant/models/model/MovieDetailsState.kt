package com.ant.models.model

import com.ant.models.entities.MovieDetails

data class MovieDetailsState(
    val movieDetails: MovieDetails? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)