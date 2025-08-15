package com.sanaa.presentation.base

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


    protected fun <T> tryToExecute(
        block: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (NovixAppException) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        val handler = createExceptionHandler(onError)
        viewModelScope.launch(dispatcher + handler) {
            val result = block()
            onSuccess(result)
        }
    }



    protected fun <R> tryToCollect(
        block: suspend () -> Flow<R>,
        onCollect: suspend (R) -> Unit,
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher
    ) {
        val handler = createExceptionHandler(onError)
        viewModelScope.launch(dispatcher + handler) {
            block()
                .catch { e ->
                    // Handle flow errors
                    val error = e.toNovixAppException()
                    onError(error)
                }
                .collect { value ->
                    try {
                        onCollect(value)
                    } catch (e: Throwable) {
                        // Handle collection errors
                        onError(e.toNovixAppException())
                    }
                }
        }
    }

    protected fun emitEffect(effect: E) {
        val now = System.currentTimeMillis()

        val effectType = effect!!::class
        val lastEffectType = lastEffect?.let { it::class }

        if (effectType == lastEffectType && now - lastTime < effectDebounceMs) return

        lastEffect = effect
        lastTime = now

        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }


    private fun createExceptionHandler(onError: (NovixAppException) -> Unit) =
        CoroutineExceptionHandler { _, exception ->
            onError(exception.toNovixAppException())
        }

    private fun Throwable.toNovixAppException(): NovixAppException = when (this) {
        is NovixAppException -> this
        else -> NovixAppException(message ?: "Unknown error")
    }

}