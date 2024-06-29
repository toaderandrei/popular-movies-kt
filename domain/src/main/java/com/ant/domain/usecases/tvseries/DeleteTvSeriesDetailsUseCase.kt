package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShowDetails
import com.ant.models.source.repositories.tvseries.DeleteTvSeriesDetailsRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTvSeriesDetailsUseCase @Inject constructor(
    private val repository: DeleteTvSeriesDetailsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<TvShowDetails, Unit>(dispatcher) {
    override suspend fun execute(parameters: TvShowDetails) {
        return repository.performRequest(parameters)
    }
}