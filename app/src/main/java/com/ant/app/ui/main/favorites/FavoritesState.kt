package com.ant.app.ui.main.favorites

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

data class FavoritesState(
    val tvSeriesFavored: List<TvShow> = emptyList(),
    val isTvSeriesFavoredLoading: Boolean = false,
    val isTvSeriesFavoredError: Boolean = false,
    val moviesFavored: List<MovieData> = emptyList(),
    val isMoviesFavoredLoading: Boolean = false,
    val isMoviesFavoredError: Boolean = false,
)