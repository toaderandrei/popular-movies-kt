package com.ant.models.model

import com.ant.models.entities.MovieData

data class MoviesListState(
    val popularMovies: UIState<List<MovieData>> = UIState(),
    val topMovies: UIState<List<MovieData>> = UIState(),
    val nowPlayingMovies: UIState<List<MovieData>> = UIState(),
    val upcomingMovies: UIState<List<MovieData>> = UIState()
)


