package com.ant.feature.tvshow.details

import com.ant.models.entities.TvShowDetails

data class TvShowDetailsUiState(
    val isLoading: Boolean = true,
    val tvShowDetails: TvShowDetails? = null,
    val error: String? = null,
    val isFavorite: Boolean = false,
)
