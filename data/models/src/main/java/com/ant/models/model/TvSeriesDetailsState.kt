package com.ant.models.model

import com.ant.models.entities.TvShowDetails

data class TvSeriesDetailsState(
    val tvSeriesDetails: TvShowDetails? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
)