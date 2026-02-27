package com.ant.feature.favorites

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

/**
 * UI state for the Favorites screen
 */
data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favoriteMovies: List<MovieData> = emptyList(),
    val favoriteTvShows: List<TvShow> = emptyList(),
    val selectedTab: FavoriteTab = FavoriteTab.MOVIES,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val syncingIds: Set<Long> = emptySet(),
    val snackbarMessage: String? = null,
)

enum class FavoriteTab {
    MOVIES,
    TV_SHOWS
}
