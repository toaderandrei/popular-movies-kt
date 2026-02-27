package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.movies.LoadMovieDetailsSummaryRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieDetails
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsUseCase @Inject constructor(
    private val repository: LoadMovieDetailsSummaryRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: RequestType.MovieRequestDetails): Flow<Result<MovieDetails>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
        }
    }
}
