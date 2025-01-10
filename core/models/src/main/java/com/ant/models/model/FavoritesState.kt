package com.ant.models.model

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow

data class FavoritesState(
    val tvSeriesFavored: UIState<List<TvShow>> = UIState(),
    val moviesFavored: UIState<List<MovieData>> = UIState(),
)