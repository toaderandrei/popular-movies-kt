package com.ant.feature.tvshow.category

import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType

data class TvShowCategoryUiState(
    val categoryType: TvShowType = TvShowType.POPULAR,
    val tvShows: List<TvShow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)
