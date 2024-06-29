package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.tvseries.LoadTvSeriesDetailsSummaryRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvSeriesDetailsUseCase @Inject constructor(
    private val repository: LoadTvSeriesDetailsSummaryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Repository.Params<RequestType.TvSeriesRequestDetails>, TvShowDetails>(dispatcher) {

    override suspend fun execute(parameters: Repository.Params<RequestType.TvSeriesRequestDetails>): TvShowDetails {
        return repository.performRequest(parameters)
    }
}