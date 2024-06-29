package com.ant.domain.usecases.movies

import com.ant.models.entities.MovieDetails
import com.ant.models.source.repositories.movies.SaveMovieDetailsRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveMovieDetailsUseCase @Inject constructor(
    private val repository: SaveMovieDetailsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<MovieDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: MovieDetails): Unit {
        return repository.performRequest(parameters)
    }
}