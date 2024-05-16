package com.ant.app.ui.base

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseViewModel<S>(
    initialState: S
) : ViewModel() {
    val state = MutableStateFlow(initialState)
    private val lock = Mutex()

    /**
     * Returns a snapshot of the current state.
     */
    fun currentState(): S? = state.value

    val stateAsLiveData: LiveData<S>
        get() = state.asLiveData()

    val stateAsFlow: StateFlow<S>
        get() = state.asStateFlow()

    protected suspend fun <T> Flow<T>.collectAndSetState(reducer: S.(T) -> S) {
        return collect { item ->
            lock.withLock {
                state.value = reducer(state.value, item)
            }
        }
    }
}