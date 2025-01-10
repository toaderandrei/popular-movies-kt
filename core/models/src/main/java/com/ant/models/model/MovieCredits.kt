package com.ant.models.model

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieReview
import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @field:SerializedName("cast")
    val movieCastList: List<MovieCast>?,

    @field:SerializedName("crew")
    val movieCrew: List<MovieCrew>?,

    @field:SerializedName("review")
    val reviews: List<MovieReview>?
)
