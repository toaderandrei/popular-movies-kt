package com.ant.data.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.database.database.MoviesDb
import com.ant.data.repositories.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LoadFavoredMovieListRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) : Repository<Boolean, List<MovieData>> {
    override suspend fun performRequest(params: Boolean): List<MovieData> {
        return moviesDb.moviesDao().loadFavoredMovies(params)
    }
}