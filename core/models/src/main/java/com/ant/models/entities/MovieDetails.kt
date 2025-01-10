package com.ant.models.entities

data class MovieDetails(
    val movieData: MovieData,
    val videos: List<MovieVideo>? = emptyList(),
    val movieCasts: List<MovieCast>? = emptyList(),
    val reviews: List<MovieReview>? = emptyList(),
    val movieCrewList: List<MovieCrew>? = emptyList()
)