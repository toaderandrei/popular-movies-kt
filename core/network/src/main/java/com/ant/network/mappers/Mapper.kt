package com.ant.network.mappers

interface Mapper<F, T> {
    suspend fun map(from: F): T
}