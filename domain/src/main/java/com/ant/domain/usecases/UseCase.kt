package com.ant.domain.usecases

import com.ant.models.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): Flow<Result<R>> {
        return flow {
            emit(Result.Loading)
            delay(500)
            val response = execute(parameters)
            emit(Result.Success(response))
        }.catch { emit(Result.Error(it)) }.flowOn(coroutineDispatcher)
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}
