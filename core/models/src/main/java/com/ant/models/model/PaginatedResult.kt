package com.ant.models.model

data class PaginatedResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int,
)
