package com.ant.models.source.repositories.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DeleteMovieDetailsRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<MovieDetails, Unit> {
    override suspend fun fetchData(params: MovieDetails) {
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