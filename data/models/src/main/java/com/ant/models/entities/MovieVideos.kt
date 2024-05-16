package com.ant.models.entities

import androidx.room.Embedded
import androidx.room.Relation

class MovieVideos {
    @Embedded
    var movie: MovieData? = null

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    var videos: List<MovieVideo>? = null
}