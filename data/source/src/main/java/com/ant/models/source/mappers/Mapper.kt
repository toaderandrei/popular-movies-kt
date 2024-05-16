package com.ant.models.source.mappers

interface Mapper<F, T> {
    suspend fun map(from: F): T
}