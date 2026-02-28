package com.ant.domain.usecases.tvseries

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.TvSeriesRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.TvShow
import com.ant.models.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadFavoredTvSeriesUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: Boolean): Flow<Result<List<TvShow>>> {
        return resultFlow(dispatcher) {
            tvSeriesRepository.getFavoredTvSeries(parameters)
        }
    }
}
