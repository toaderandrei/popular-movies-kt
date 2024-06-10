
package com.ant.models.source.database

import com.ant.models.source.dao.*

interface TmdbDatabase {
    fun moviesDao(): MoviesDao
    fun tvSeriesDao(): TvSeriesDao
    fun movieReviewsDao(): MovieReviewsDao
    fun movieCrewDao(): MovieCrewDao
    fun movieCastDao(): MovieCastsDao
    fun movieVideosDao(): MovieVideosDao
}
