package com.ant.feature.tvshow

import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

/**
 * UI state for the TV Shows screen
 */
data class TvShowUiState(
    val isLoading: Boolean = false,
    val tvShows: List<TvShow> = emptyList(),
    val selectedCategory: TvShowType = TvShowType.POPULAR,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)
