package com.ant.common.exceptions

class TmdbException(override val message: String, errorCause: Throwable? = null) : RuntimeException(message, errorCause)