package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.models.entities.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteMovieDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: MovieDetails) {
        moviesDb.moviesDao().deleteMovieById(params.movieData.id)
            .also {
                params.movieCasts?.let {
                    moviesDb.movieCastDao().deleteMovieCastsById(params.movieData.id)
                }
            }.also {
                params.videos?.let {
                    moviesDb.movieVideosDao().deleteMovieVideosById(params.movieData.id)
                }
            }
    }
}