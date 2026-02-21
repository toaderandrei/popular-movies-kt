package com.ant.feature.movies.category

import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

data class MovieCategoryUiState(
    val categoryType: MovieType = MovieType.POPULAR,
    val movies: List<MovieData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)
