
package com.ant.models.source.database

import com.ant.models.source.dao.*

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
