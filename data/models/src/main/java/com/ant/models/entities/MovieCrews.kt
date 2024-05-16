package com.ant.models.entities

import androidx.room.Embedded
import androidx.room.Relation

class MovieCrews {
    @Embedded
    var movie: MovieData? = null

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    var crewList: List<MovieCrew>? = null
}