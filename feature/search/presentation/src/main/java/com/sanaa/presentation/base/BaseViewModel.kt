package com.sanaa.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E>(
    initialState: T,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state.asStateFlow()

    private val _effectChannel = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effectChannel.receiveAsFlow()

    private var lastEffect: E? = null
    private var lastTime = 0L
    private val effectDebounceMs = 500L

    internal fun updateState(updater: T.() -> T) {
        _state.update(updater)
    }

    private fun createExceptionHandler(onError: (Throwable) -> Unit) =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable, "Unhandled coroutine exception")
            onError(throwable)
        }

    protected fun <R> tryToExecute(
        block: suspend () -> R,
        onSuccess: (R) -> Unit = {},
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher
    ) {
        val exceptionHandler = createExceptionHandler(onError)

        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                val result = block()
                onSuccess(result)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    protected fun <R> tryToCollect(
        block: suspend () -> Flow<R>,
        onCollect: suspend (R) -> Unit,
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher
    ) {
        val exceptionHandler = createExceptionHandler(onError)
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                block()
                    .catch { e -> onError(e) }
                    .collectLatest { value ->
                        try {
                            onCollect(value)
                        } catch (e: Throwable) {
                            onError(e)
                        }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    protected fun emitEffect(effect: E) {
        val now = System.currentTimeMillis()
        if (effect == lastEffect && now - lastTime < effectDebounceMs) return

        lastEffect = effect
        lastTime = now

        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }
}