package com.ant.models.source.repositories.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SaveMovieDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<MovieDetails, Unit> {
    override suspend fun  performRequest(params: MovieDetails) {
        moviesDb.moviesDao().insert(params.movieData.copy(favored = true))
            .also {
                params.movieCasts?.let {
                    val movieCasts = it.map { movieCast ->
                        movieCast.copy(movieId = params.movieData.id)
                    }
                    moviesDb.movieCastDao().insertAll(movieCasts)
                }
            }.also {
                params.videos?.let {
                    val videosToInsert = it.map { video ->
                        video.copy(movieId = params.movieData.id)
                    }
                    moviesDb.movieVideosDao().insertAll(videosToInsert)
                }
            }
    }
}