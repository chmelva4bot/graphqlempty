package cz.applifting.graphqlempty

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class Reducer<S : State, E : Event>(initialVal: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialVal) // private mutable state flow with initial state
    val state: StateFlow<S> // publicly exposed as read-only state flow
        get() = _state

    fun sendEvent(event: E) {
        reduce(_state.value, event)
    }

    protected fun setState(newState: S) {
        val success = _state.tryEmit(newState)
    }

    protected abstract fun reduce(oldState: S, event: E)
}

interface State

interface Event
