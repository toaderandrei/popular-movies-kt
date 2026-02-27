package com.ant.feature.tvshow

import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

/**
 * UI state for the TV Shows screen
 */
data class TvShowUiState(
    val isLoading: Boolean = false,
    val tvShowSections: Map<TvShowType, TvShowSection> = emptyMap(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

/**
 * Represents a section of TV shows for a specific category
 */
data class TvShowSection(
    val category: TvShowType,
    val tvShows: List<TvShow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
