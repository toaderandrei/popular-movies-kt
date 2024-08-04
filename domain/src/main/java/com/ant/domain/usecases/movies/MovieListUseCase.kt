package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.movies.LoadMovieListRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository: LoadMovieListRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Repository.Params<RequestType.MovieRequest>, List<MovieData>>(dispatcher) {

    override suspend fun execute(parameters: Repository.Params<RequestType.MovieRequest>): List<MovieData> {
        return repository.performRequest(parameters)
    }
}