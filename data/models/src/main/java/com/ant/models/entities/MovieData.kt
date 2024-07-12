package com.ant.models.entities

import androidx.room.*
import com.ant.models.extensions.toDateString
import com.ant.models.extensions.toTwoDigitNumber
import java.util.*

@Entity(
    tableName = "moviedata",
    indices = [
        Index(value = ["id"], unique = true)
    ],
)
data class MovieData(
    @PrimaryKey
    @ColumnInfo(name = "id") override val id: Long = 0,

    @ColumnInfo(name = "title")
    override val name: String? = null,

    @ColumnInfo(name = "original_title")
    val originalTitle: String? = null,

    @ColumnInfo(name = "homepage")
    val homepage: String? = null,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int? = null,

    @ColumnInfo(name = "overview")
    val overview: String? = null,

    @ColumnInfo(name = "runtime")
    val runtime: String? = null,

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    val backDropPath: String? = null,

    @ColumnInfo(name = "release_date")
    val _releaseDate: Date? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double? = null,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String? = null,

    @ColumnInfo(name = "genres")
    var _genres: List<String>? = emptyList(),

    @ColumnInfo(name = "genres_ids")
    var _genres_ids: List<Int>? = emptyList(),

    @ColumnInfo(name = "favored")
    var favored: Boolean? = null,
) : TmdbEntity {
    @Ignore
    constructor() : this(id = 0)

    @delegate:Ignore
    val genres by lazy(LazyThreadSafetyMode.NONE) {
        _genres?.joinToString(", ")
    }

    @delegate:Ignore
    val releaseDate by lazy(LazyThreadSafetyMode.NONE) {
        _releaseDate?.toDateString(useDifferentFormat = true)
    }

    @delegate:Ignore
    val voteAverageRounded by lazy(LazyThreadSafetyMode.NONE) {
        voteAverage?.toTwoDigitNumber()
    }
}