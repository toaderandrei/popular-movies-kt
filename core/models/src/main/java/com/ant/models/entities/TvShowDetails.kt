package com.ant.models.entities

data class TvShowDetails(
    val tvSeriesData: TvShow,
    val videos: List<MovieVideo>? = emptyList(),
    val tvSeriesCasts: List<MovieCast>? = emptyList(),
    val movieCrewList: List<MovieCrew>? = emptyList()
)