package com.ant.models.source.repositories

import com.ant.models.request.RequestType

interface Repository<in P, out R> {

    /**
     * Load movies.
     */
    suspend fun fetchData(params: P): R

    data class Params<out T: RequestType>(
        val request: T,
        val page: Int? = 1,
    )
}