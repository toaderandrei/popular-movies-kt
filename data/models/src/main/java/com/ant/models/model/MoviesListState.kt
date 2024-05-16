package com.ant.models.model

import com.ant.models.entities.MovieData

data class MoviesListState(
    val items: List<MovieData>? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)