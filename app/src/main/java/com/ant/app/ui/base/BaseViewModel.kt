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
    initialState: S,
) : ViewModel() {
    private val state = MutableStateFlow(initialState)
    private val lock = Mutex()
    protected var job: Job? = null

    val stateAsFlow: StateFlow<S>
        get() = state.asStateFlow()

    protected suspend fun <T> Flow<T>.collectAndSetState(reducer: S.(T) -> S) {
        return collectLatest { item ->
            lock.withLock {
                state.value = reducer(state.value, item)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}