package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.source.repositories.tvseries.SaveTvSeriesDetailsRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveTvSeriesDetailsUseCase @Inject constructor(
    private val repository: SaveTvSeriesDetailsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TvShowDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: TvShowDetails): Unit {
        return repository.fetchData(parameters)
    }
}