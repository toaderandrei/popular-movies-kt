package com.ant.models.model

import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.model.MoviesState

data class FavoritesState(
    val tvSeriesFavored: MoviesState<List<TvShow>> = MoviesState(),
    val moviesFavored: MoviesState<List<MovieData>> = MoviesState(),
)