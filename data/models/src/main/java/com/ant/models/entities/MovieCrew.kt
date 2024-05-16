package com.ant.models.entities

import androidx.room.*

@Entity(
    tableName = "MovieCrew",
    indices = [
        Index(value = ["movie_id"])],
    foreignKeys = [ForeignKey(
        entity = MovieData::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class MovieCrew(
    @PrimaryKey
    @ColumnInfo(name = "id") override val id: Long = 0,

    @ColumnInfo(name = "credits_id") val creditsId: Int? = null,

    @ColumnInfo(name = "movie_id") var movieId: Long? = null,

    @ColumnInfo(name = "department") val department: String? = null,

    @ColumnInfo(name = "job") val job: String? = null,

    @ColumnInfo(name = "profile_path") val profilePath: String? = null
) : TmdbEntity