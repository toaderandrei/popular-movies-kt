package com.ant.data.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.database.database.MoviesDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadFavoredMovieListRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: Boolean): List<MovieData> {
        return moviesDb.moviesDao().loadFavoredMovies(params)
    }
}