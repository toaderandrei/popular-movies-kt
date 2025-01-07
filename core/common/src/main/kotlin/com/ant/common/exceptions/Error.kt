package com.ant.common.exceptions

data class Error(
    val statusCode: Int = 0,
    val e: Throwable,
    val statusMessage: String? = null
)