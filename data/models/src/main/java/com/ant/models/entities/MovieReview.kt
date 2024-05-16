package com.ant.models.entities

import androidx.room.*

@Entity(
    tableName = "MovieReview",
    indices = [
        Index(value = ["movie_id"])],
    foreignKeys = [
        ForeignKey(
            entity = MovieData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movie_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class MovieReview(
    @PrimaryKey
    @ColumnInfo(name = "id") override val id: Long = 0,

    @ColumnInfo(name = "author") val author: String?,

    @ColumnInfo(name = "content") val content: String?,

    @ColumnInfo(name = "url") val url: String?,

    @ColumnInfo(name = "movie_id") var movieId: Long = 0,

    @ColumnInfo(name = "tmdb_id") val tmdbId: Int? = null,
) : TmdbEntity