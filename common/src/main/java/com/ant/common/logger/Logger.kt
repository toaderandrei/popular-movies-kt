package com.ant.common.logger

interface Logger {

    /** Log a verbose message with optional format args.  */
    fun v(message: String)

    /** Log a debug message.  */
    fun d(message: String)

    /** Log an info message.  */
    fun i(message: String)

    /** Log a debug exception and a message args.  */
    fun e(t: Throwable, message: String)

    /** Log a debug exception.  */
    fun e(t: Throwable)
}