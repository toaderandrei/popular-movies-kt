package com.ant.models.model

import com.ant.models.entities.MovieData

data class MoviesListState(
    val popularMovies: MoviesState<List<MovieData>> = MoviesState(),
    val topMovies: MoviesState<List<MovieData>> = MoviesState(),
    val nowPlayingMovies: MoviesState<List<MovieData>> = MoviesState(),
    val upcomingMovies: MoviesState<List<MovieData>> = MoviesState()
)


