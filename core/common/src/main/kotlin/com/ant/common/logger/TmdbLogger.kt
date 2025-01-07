package com.ant.common.logger

import timber.log.Timber
import javax.inject.Inject

class TmdbLogger @Inject constructor() : Logger {

    fun init(debug: Boolean = true) {
        if (debug) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun v(message: String) {
        Timber.v(message)
    }

    override fun d(message: String) {
        Timber.d(message)
    }

    override fun i(message: String) {
        Timber.i(message)
    }

    override fun e(t: Throwable, message: String) {
        Timber.e(t, message)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }
}