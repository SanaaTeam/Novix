package com.sanaa.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E>(
    initialState: T,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()


    internal fun updateState(updater: (T) -> T) {
        _state.update(updater)
    }

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (exception: Exception) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
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

    protected fun <T> tryToCollect(
        callee: suspend () -> Flow<T>,
        onCollect: suspend (T) -> Unit,
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                callee().catch { onError(it) }
                    .collectLatest { result ->
                        onCollect(result)
                    }
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}