package com.ant.feature.movies

import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

/**
 * UI state for the Movies screen
 */
data class MoviesUiState(
    val isLoading: Boolean = false,
    val movieSections: Map<MovieType, MovieSection> = emptyMap(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

/**
 * Represents a section of movies for a specific category
 */
data class MovieSection(
    val category: MovieType,
    val movies: List<MovieData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
