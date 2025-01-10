package com.ant.models.entities

import androidx.room.*

@Entity(
    tableName = "MovieCast",
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
data class MovieCast(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") override var id: Long = 0,

    @ColumnInfo(name = "credit_id")
    var creditId: Int? = null,

    @ColumnInfo(name = "cast_id")
    var cast_id: Int? = null,

    @ColumnInfo(name = "movie_id")
    var movieId: Long? = 0,

    @ColumnInfo(name = "character")
    override val name: String? = null,

    @ColumnInfo(name = "order")
    var order: Int? = 0,

    @ColumnInfo(name = "profile_path")
    var profileImagePath: String? = null
) : TmdbEntity