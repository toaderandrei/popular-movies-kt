package com.ant.domain.usecases.search

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.SearchRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTvShowUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(params: RequestType.SearchTvShowRequest): Flow<Result<List<TvShow>>> {
        return resultFlow(dispatcher) {
            repository.searchTvShows(params)
        }
    }
}
