package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.tvseries.LoadTvSeriesListRepository
import com.ant.domain.usecases.UseCase
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvShowListUseCase @Inject constructor(
    private val repository: LoadTvSeriesListRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RequestType.TvShowRequest, List<TvShow>>(dispatcher) {

    override suspend fun execute(parameters: RequestType.TvShowRequest): List<TvShow> {
        return repository.performRequest(parameters)
    }
}