package com.ant.feature.search

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val movieResults: List<MovieData> = emptyList(),
    val tvShowResults: List<TvShow> = emptyList(),
    val selectedTab: SearchTab = SearchTab.MOVIES,
    val error: String? = null,
)

enum class SearchTab {
    MOVIES,
    TV_SHOWS
}
