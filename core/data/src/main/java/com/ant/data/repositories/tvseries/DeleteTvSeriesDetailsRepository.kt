package com.ant.data.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.database.database.MoviesDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeleteTvSeriesDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: TvShowDetails) {
        moviesDb.tvSeriesDao().deleteTvSeriesById(params.tvSeriesData.id)
            .also {
                params.tvSeriesCasts?.let {
                    moviesDb.movieCastDao().deleteMovieCastsById(params.tvSeriesData.id)
                }
            }.also {
                params.videos?.let {
                    moviesDb.movieVideosDao().deleteMovieVideosById(params.tvSeriesData.id)
                }
            }
    }
}