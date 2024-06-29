package com.ant.models.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ant.models.model.UserData
import com.ant.models.source.converters.TmdbTypeConverters
import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieReview
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.TvShow

@Database(
    entities = [
        MovieData::class,
        TvShow::class,
        MovieCast::class,
        MovieReview::class,
        MovieCrew::class,
        MovieVideo::class,
   ],
    version = 36,
    exportSchema = false
)
@TypeConverters(
    TmdbTypeConverters::class,
)
abstract class MoviesDb : RoomDatabase(), TmdbDatabase