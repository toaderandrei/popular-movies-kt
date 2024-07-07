package com.ant.app.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseViewModel<S>(
    initialValue: S
) : ViewModel() {
    private val _state = MutableStateFlow<S>(initialValue)
    private val lock = Mutex()
    protected var job: Job? = null

    val stateAsFlow: StateFlow<S>
        get() = _state.asStateFlow()

    protected suspend fun <T> Flow<T>.collectAndSetState(reducer: S.(T) -> S) {
        return collectLatest { item ->
            lock.withLock {
                _state.value?.let {
                    _state.value = reducer(it, item)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}