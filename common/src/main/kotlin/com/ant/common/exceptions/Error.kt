package com.ant.common.exceptions

data class Error(val status_code: Int = 0,
                 val e: Throwable,
                 val status_message: String? = null)