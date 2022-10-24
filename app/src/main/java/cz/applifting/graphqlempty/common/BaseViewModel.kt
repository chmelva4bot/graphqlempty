package cz.applifting.graphqlempty.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class  BaseViewModel<T : State, in E : Event, in A : Action> : ViewModel(), CoroutineScope {
    abstract val state: Flow<T>
    abstract val reducer: Reducer<T, @UnsafeVariance E>

    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO

    private val actionFlow: MutableSharedFlow<A> = MutableSharedFlow()

    abstract fun handleAction(action: A)

    init {
        subscribeToActions()
    }

    private fun subscribeToActions() {
        viewModelScope.launch {
            actionFlow.collect { action ->
                handleAction(action)
            }
        }
    }

    protected fun sendEvent(event: E) {
        reducer.sendEvent(event)
    }

    fun sendAction(action: A) {
        viewModelScope.launch { actionFlow.emit(action) }
    }
}
