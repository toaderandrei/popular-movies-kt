package com.ant.models.source.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DeleteTvSeriesDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<TvShowDetails, Unit> {
    override suspend fun fetchData(params: TvShowDetails) {
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