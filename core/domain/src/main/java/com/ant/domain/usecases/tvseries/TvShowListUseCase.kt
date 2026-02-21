package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.tvseries.LoadTvSeriesListRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowListUseCase @Inject constructor(
    private val repository: LoadTvSeriesListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: RequestType.TvShowRequest): Flow<Result<List<TvShow>>> {
        return resultFlow(dispatcher) {
            repository.performRequest(parameters)
        }
    }
}
