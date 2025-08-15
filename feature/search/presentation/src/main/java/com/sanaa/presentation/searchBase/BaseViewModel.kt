package com.sanaa.presentation.searchBase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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


    internal fun updateState(updater: T.() -> T) {
        _state.update(updater)
    }

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (exception: NovixAppException) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        val handler = createExceptionHandler(onError)

        viewModelScope.launch(dispatcher + handler) {
            val result = callee()
            onSuccess(result)
        }
    }

    protected fun <T> tryToCollect(
        callee: suspend () -> Flow<T>,
        onCollect: suspend (T) -> Unit,
        onError: (exception: NovixAppException) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        val handler = createExceptionHandler(onError)

        viewModelScope.launch(dispatcher + handler) {
            callee().collectLatest { result ->
                onCollect(result)
            }
        }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }


    private fun createExceptionHandler(onError: (NovixAppException) -> Unit) =
        CoroutineExceptionHandler { _, exception ->
            onError(
                when (exception) {
                    is NovixAppException -> exception
                    else -> NovixAppException(exception.message)
                }
            )
        }
}