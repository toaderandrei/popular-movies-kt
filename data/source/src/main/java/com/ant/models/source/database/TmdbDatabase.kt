/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
