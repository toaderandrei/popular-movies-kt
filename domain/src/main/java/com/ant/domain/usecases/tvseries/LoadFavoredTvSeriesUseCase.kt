package com.ant.domain.usecases.tvseries

import com.ant.models.entities.TvShow
import com.ant.models.source.repositories.tvseries.LoadFavoredTvSeriesListRepository
import com.ant.domain.qualifiers.IoDispatcher
import com.ant.domain.usecases.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoadFavoredTvSeriesUseCase @Inject constructor(
    private val loadFavoredMoviesRepository: LoadFavoredTvSeriesListRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Boolean, List<TvShow>>(dispatcher) {
    override suspend fun execute(parameters: Boolean): List<TvShow> {
        return loadFavoredMoviesRepository.performRequest(parameters)
    }
}