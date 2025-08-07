package com.sanaa.presentation.onbordingBase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE>(
    initialState: STATE,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    protected fun updateState(update: (STATE) -> STATE) {
        _state.value = update(_state.value)
    }

    protected fun tryToExecute(
        callee: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            callee()
        }
    }
}