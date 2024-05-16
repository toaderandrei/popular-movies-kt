package com.ant.app.application

import com.ant.common.di.Initializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards Initializer>
) {
    fun init() {
        initializers.forEach {
            it.init()
        }
    }
}