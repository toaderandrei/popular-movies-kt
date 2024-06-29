package com.ant.models.source.repositories.movies

import com.ant.models.entities.MovieData
import com.ant.models.source.database.MoviesDb
import com.ant.models.source.repositories.Repository
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