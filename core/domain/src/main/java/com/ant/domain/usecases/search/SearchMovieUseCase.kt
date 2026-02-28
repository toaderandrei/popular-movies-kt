package com.ant.domain.usecases.search

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.SearchRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(params: RequestType.SearchMovieRequest): Flow<Result<List<MovieData>>> {
        return resultFlow(dispatcher) {
            repository.searchMovies(params)
        }
    }
}
