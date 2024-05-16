package com.ant.models.entities

import androidx.room.*

@Entity(
    tableName = "MovieVideo",
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
data class MovieVideo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") override var id: Long = 0,

    @ColumnInfo(name = "key") val key: String?,

    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "iso_639_1") val iso_639_1: String?,

    @ColumnInfo(name = "iso_3166_1") val iso_3166_1: String?,

    @ColumnInfo(name = "site") val site: String?,

    @ColumnInfo(name = "size") val size: Int?,

    @ColumnInfo(name = "type") val type: VideoType?,

    @ColumnInfo(name = "movie_id") var movieId: Long = 0,
) : TmdbEntity