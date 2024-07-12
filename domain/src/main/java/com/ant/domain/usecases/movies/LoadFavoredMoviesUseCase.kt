package com.ant.domain.usecases.movies

import com.ant.models.entities.MovieData
import com.ant.models.source.repositories.movies.LoadFavoredMovieListRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoadFavoredMoviesUseCase @Inject constructor(
    private val loadFavoredMoviesRepository: LoadFavoredMovieListRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Boolean, List<MovieData>>(dispatcher) {
    override suspend fun execute(parameters: Boolean): List<MovieData> {
        return loadFavoredMoviesRepository.performRequest(parameters)
    }
}