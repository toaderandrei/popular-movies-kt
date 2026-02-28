package com.ant.feature.movies.category

import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType

data class MovieCategoryUiState(
    val categoryType: MovieType = MovieType.POPULAR,
    val movies: List<MovieData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val isLoadingMore: Boolean = false,
) {
    val canLoadMore: Boolean
        get() = currentPage < totalPages && !isLoadingMore && !isLoading && error == null
}
