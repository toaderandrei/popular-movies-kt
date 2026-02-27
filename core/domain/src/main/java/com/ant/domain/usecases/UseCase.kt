package com.ant.domain.usecases

import com.ant.models.model.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Creates a Flow that wraps a suspend block in Loading/Success/Error states.
 * Use this instead of inheriting from an abstract UseCase class.
 */
fun <R> resultFlow(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> R,
): Flow<Result<R>> {
    return flow {
        emit(Result.Loading)
        val response = block()
        emit(Result.Success(response))
    }.catch { e ->
        if (e is CancellationException) throw e
        emit(Result.Error(e))
    }.flowOn(dispatcher)
}
