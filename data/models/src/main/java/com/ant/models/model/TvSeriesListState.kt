package com.ant.models.model

import com.ant.models.entities.TvShow

data class TvSeriesListState(
    val items: List<TvShow>? = emptyList(),
    val loading: Boolean = false,
    val error: Throwable? = null
)