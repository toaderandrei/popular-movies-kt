package com.ant.models.model

import com.ant.models.entities.TvShow

data class TvShowListState(
    val popularTvSeries: MoviesState<List<TvShow>> = MoviesState(),
    val topRated: MoviesState<List<TvShow>> = MoviesState(),
    val onTvNow: MoviesState<List<TvShow>> = MoviesState(),
    val airingToday: MoviesState<List<TvShow>> = MoviesState()
)
