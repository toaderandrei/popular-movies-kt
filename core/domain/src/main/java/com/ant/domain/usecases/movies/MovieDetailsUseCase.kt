package com.ant.domain.usecases.movies

import com.ant.models.request.RequestType
import com.ant.models.entities.MovieDetails
import com.ant.data.repositories.Repository
import com.ant.data.repositories.movies.LoadMovieDetailsSummaryRepository
import com.ant.common.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MovieDetailsUseCase @Inject constructor(
    private val repository: LoadMovieDetailsSummaryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RequestType.MovieRequestDetails, MovieDetails>(dispatcher) {

    override suspend fun execute(parameters: RequestType.MovieRequestDetails): MovieDetails {
        return repository.performRequest(parameters)
    }
}