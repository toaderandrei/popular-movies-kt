package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.movies.LoadMovieListRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListUseCase @Inject constructor(
    private val repository: LoadMovieListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: RequestType.MovieRequest): Flow<Result<List<MovieData>>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
        }
    }
}
