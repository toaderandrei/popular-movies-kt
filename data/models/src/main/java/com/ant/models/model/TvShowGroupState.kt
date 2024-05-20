package com.ant.models.model

import com.ant.models.entities.TvShow

data class TvShowGroupState(
    val popularTvSeriesItems: List<TvShow> = emptyList(),
    val isPopularTvSeriesLoading: Boolean = false,
    val isPopularTvSeriesError: Boolean = false,
    val popularTvSeriesError: Throwable? = null,

    val onTvNowTvSeriesItems: List<TvShow> = emptyList(),
    val isOnTvNowTvSeriesItemsLoading: Boolean = false,
    val isOnTvNowTvSeriesError: Boolean = false,
    val onTvNowTvSeriesError: Throwable? = null,

    val onAiringTodayTvSeriesItems: List<TvShow> = emptyList(),
    val isAiringTodayTvSeriesLoading: Boolean = false,
    val isAiringTodayTvSeriesError: Boolean = false,
    val airingtodayTvSeriesError: Throwable? = null,

    val topRatedTvSeriesItems: List<TvShow> = emptyList(),
    val isTopRatedTvSeriesLoading: Boolean = false,
    val isTopRatedTvSeriesError: Boolean = false,
    val topRatedTvSeriesError: Throwable? = null,
)