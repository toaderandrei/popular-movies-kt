package com.ant.models.entities

import androidx.room.Embedded
import androidx.room.Relation

class MovieCasts {
    @Embedded
    var movie: MovieData? = null

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    var castList: List<MovieCast>? = null
}