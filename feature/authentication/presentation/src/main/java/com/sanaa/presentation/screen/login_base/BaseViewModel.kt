package com.sanaa.presentation.screen.login_base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import exceptions.IdentityException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E>(
    initialState: T,
    val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _state: MutableStateFlow<T> by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<T> by lazy { _state.asStateFlow() }

    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    protected fun updateState(updater: T.() -> T) {
        _state.update(updater)
    }

    protected fun <T> tryToExecute(
        block: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (exception: IdentityException) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        val handler = createExceptionHandler(onError)
        viewModelScope.launch(dispatcher + handler) {
            val result = block()
            onSuccess(result)
        }
    }


    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun createExceptionHandler(onError: (IdentityException) -> Unit) =
        CoroutineExceptionHandler { _, exception ->
            onError(
                when (exception) {
                    is IdentityException -> exception
                    else -> IdentityException()
                }
            )
        }
}