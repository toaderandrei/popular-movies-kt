package com.ant.common.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class MutableCustomStateFlow<T> private constructor(private val sharedFlow: MutableSharedFlow<T>) :
    Flow<T> by sharedFlow, CustomStateFlow<T> {
    constructor() : this(
        MutableSharedFlow(
            replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    )

    constructor(initialValue: T) : this() {
        setValue(initialValue)
    }

    override fun getValue(): T? {
        return sharedFlow.replayCache.firstOrNull()
    }

    fun setValue(newValue: T) {
        sharedFlow.tryEmit(newValue)
    }
}