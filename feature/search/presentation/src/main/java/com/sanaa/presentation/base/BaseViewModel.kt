package com.sanaa.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T>(initialState: T) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    protected fun updateState(updater: (T) -> T) {
        _state.update(updater)
    }

    protected fun <R> tryToExecute(
        callee: suspend () -> R,
        onSuccess: (R) -> Unit,
        onError: (Exception) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = callee()
                onSuccess(result)
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }
}