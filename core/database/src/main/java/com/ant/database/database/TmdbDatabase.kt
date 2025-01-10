package com.ant.database.database

import com.ant.database.dao.MovieCastsDao
import com.ant.database.dao.MovieCrewDao
import com.ant.database.dao.MovieReviewsDao
import com.ant.database.dao.MovieVideosDao
import com.ant.database.dao.MoviesDao
import com.ant.database.dao.TvSeriesDao

interface TmdbDatabase {
    fun moviesDao(): MoviesDao

    /**
     * @return [TvSeriesDao] instance
     */
    fun tvSeriesDao(): TvSeriesDao

    /**
     * @return [MovieReviewsDao] instance
     */
    fun movieReviewsDao(): MovieReviewsDao

    /**
     * @return [MovieCrewDao] instance
     */
    fun movieCrewDao(): MovieCrewDao

    /**
     * @return [MovieCastsDao] instance
     */
    fun movieCastDao(): MovieCastsDao

    /**
     * @return [MovieVideosDao] instance
     */
    fun movieVideosDao(): MovieVideosDao
}
