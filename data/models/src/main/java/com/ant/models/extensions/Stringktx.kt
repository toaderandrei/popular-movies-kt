package com.ant.models.extensions

import java.net.UnknownHostException

fun Throwable.toReadableError(): String {
    if (this is UnknownHostException) {
        return "No internet connection or host is down"
    }
    return "Unknown error"
}