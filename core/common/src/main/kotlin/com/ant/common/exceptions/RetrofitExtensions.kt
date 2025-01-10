package com.ant.common.exceptions

import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) {
        throw HttpException(this)
    }
    return body()!!
}

suspend fun <T> withRetry(
    defaultDelay: Long = 50,
    // magic number chosen wisely. 3 is the charm.
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = ::retryIfError,
    block: suspend () -> T
): T {
    repeat(maxAttempts) { attempt ->
        val response = runCatching { block() }

        when {
            response.isSuccess -> return response.getOrThrow()
            response.isFailure -> {
                val exception = response.exceptionOrNull()!!

                // The response failed, so lets see if we should retry again
                if (attempt == maxAttempts - 1 || !shouldRetry(exception)) {
                    throw exception
                }

                val nextDelay = attempt * defaultDelay
                delay(nextDelay)
            }
        }
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

private fun retryIfError(throwable: Throwable) = when (throwable) {
    is HttpException -> throwable.code() == 429
    is IOException -> true
    else -> false
}
