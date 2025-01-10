package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.tvseries.LoadTvSeriesDetailsSummaryRepository
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.TvShowDetails
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvSeriesDetailsUseCase @Inject constructor(
    private val repository: LoadTvSeriesDetailsSummaryRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RequestType.TvSeriesRequestDetails, TvShowDetails>(dispatcher) {

    override suspend fun execute(parameters: RequestType.TvSeriesRequestDetails): TvShowDetails {
        return repository.performRequest(parameters)
    }
}