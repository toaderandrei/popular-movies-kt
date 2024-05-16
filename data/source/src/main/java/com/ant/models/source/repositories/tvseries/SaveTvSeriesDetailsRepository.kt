package com.ant.models.source.repositories.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SaveTvSeriesDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<TvShowDetails, Unit> {
    override suspend fun  fetchData(params: TvShowDetails) {
        moviesDb.tvSeriesDao().insert(params.tvSeriesData.copy(favored = true))
//            .also {
//                params.movieCasts?.let {
//                    val movieCasts = it.map { movieCast ->
//                        movieCast.copy(movieId = params.movieData.id)
//                    }
//                    moviesDb.movieCastDao().insertAll(movieCasts)
//                }
//            }.also {
//                params.videos?.let {
//                    val videosToInsert = it.map { video ->
//                        video.copy(movieId = params.movieData.id)
//                    }
//                    moviesDb.movieVideosDao().insertAll(videosToInsert)
//                }
//            }
    }
}