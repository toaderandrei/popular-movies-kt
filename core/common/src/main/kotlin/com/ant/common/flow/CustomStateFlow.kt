package com.ant.common.flow

import kotlinx.coroutines.flow.Flow

interface CustomStateFlow<T> : Flow<T> {
    fun getValue(): T?
}