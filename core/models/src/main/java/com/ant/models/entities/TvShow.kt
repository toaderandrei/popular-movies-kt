package com.ant.models.entities

import androidx.room.*
import com.ant.models.extensions.toDateString
import com.ant.models.extensions.toTwoDigitNumber
import java.util.*

@Entity(
    tableName = "tvseriesdata",
    indices = [Index(value = ["id"], unique = true)]
)
data class TvShow(
    @PrimaryKey
    @ColumnInfo(name = "id") override val id: Long = 0,

    @ColumnInfo(name = "name")
    override val name: String?,

    @ColumnInfo(name = "original_title")
    val originalTitle: String?,

    @ColumnInfo(name = "vote")
    val voteCount: Int?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,

    @ColumnInfo(name = "backdrop_path")
    val backDropPath: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,

    @ColumnInfo(name = "status")
    val status: String? = null,

    @ColumnInfo(name = "first_air_date")
    val _firstAirDate: Date? = null,

    @ColumnInfo(name = "last_air_date")
    val _lastAirDate: Date? = null,

    @ColumnInfo(name = "number_of_seasons")
    val numberOfSeasons: Int? = null,

    @ColumnInfo(name = "number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @ColumnInfo(name = "genres_ids")
    var _genres_ids: List<Int>? = emptyList(),

    @ColumnInfo(name = "genres")
    var _genres: List<String>? = emptyList(),

    @ColumnInfo(name = "favored")
    var favored: Boolean? = false
) : TmdbEntity {

    @delegate:Ignore
    val genres by lazy(LazyThreadSafetyMode.NONE) {
        _genres?.joinToString(",")
    }

    @delegate:Ignore
    val firstAirDate by lazy(LazyThreadSafetyMode.NONE) {
        _firstAirDate?.toDateString(useDifferentFormat = true)
    }

    @delegate:Ignore
    val lastAirDate by lazy(LazyThreadSafetyMode.NONE) {
        _lastAirDate?.toDateString(useDifferentFormat = true)
    }

    @delegate:Ignore
    val voteAverageRounded by lazy(LazyThreadSafetyMode.NONE) {
        voteAverage?.toTwoDigitNumber()
    }
}