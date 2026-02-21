package com.ant.domain.usecases.movies

import com.ant.common.qualifiers.IoDispatcher
import com.ant.data.repositories.movies.LoadFavoredMovieListRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.entities.MovieData
import com.ant.models.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadFavoredMoviesUseCase @Inject constructor(
    private val loadFavoredMoviesRepository: LoadFavoredMovieListRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: Boolean): Flow<Result<List<MovieData>>> {
        return resultFlow(dispatcher) {
            loadFavoredMoviesRepository.performRequest(parameters)
        }
    }
}
