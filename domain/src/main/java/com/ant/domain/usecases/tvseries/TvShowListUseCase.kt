package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType
import com.ant.models.source.repositories.Repository
import com.ant.models.source.repositories.tvseries.LoadTvSeriesListRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvShowListUseCase @Inject constructor(
    private val repository: LoadTvSeriesListRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Repository.Params<RequestType.TvShowRequest>, List<TvShow>>(dispatcher) {

    override suspend fun execute(parameters: Repository.Params<RequestType.TvShowRequest>): List<TvShow> {
        return repository.performRequest(parameters)
    }
}