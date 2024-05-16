package com.ant.app.ui.main.movies

import com.ant.models.entities.MovieData

data class MoviesState(
    val popularItems: List<MovieData> = emptyList(),
    val isPopularMoviesLoading: Boolean = false,
    val isPopularMoviesError: Boolean = false,
    val popularMoviesError: Throwable? = null,

    val topMovieItems: List<MovieData> = emptyList(),
    val isTopMoviesLoading: Boolean = false,
    val isTopMoviesError: Boolean = false,
    val topMoviesError: Throwable? = null,

    val nowPlayingItems: List<MovieData> = emptyList(),
    val isNowPlayingMoviesLoading: Boolean = false,
    val isNowPlayingMoviesError: Boolean = false,
    val nowPlayingMoviesError: Throwable? = null,

    val upcomingItems: List<MovieData> = emptyList(),
    val isUpcomingMoviesLoading: Boolean = false,
    val isUpcomingMoviesError: Boolean = false,
    val upcomingMoviesError: Throwable? = null,
)