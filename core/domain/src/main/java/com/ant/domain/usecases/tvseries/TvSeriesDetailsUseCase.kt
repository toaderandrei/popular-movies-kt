package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.TvSeriesRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShowDetails
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvSeriesDetailsUseCase @Inject constructor(
    private val repository: TvSeriesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: RequestType.TvSeriesRequestDetails): Flow<Result<TvShowDetails>> {
        return resultFlow(dispatcher) {
            repository.getTvSeriesDetails(parameters)
        }
    }
}
