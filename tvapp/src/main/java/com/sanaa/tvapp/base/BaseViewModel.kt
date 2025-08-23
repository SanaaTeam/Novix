package com.sanaa.tvapp.base

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
        onStart: () -> Unit = {},
        block: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (exception: NovixAppException) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {
        onStart()
        viewModelScope.launch(dispatcher) {
            try {
                val result = block()
                onSuccess(result)
            } catch (exception: NovixAppException) {
                onError(exception)
            }
        }
    }

    protected fun <T> tryToCollect(
        onStart: () -> Unit = {},
        block: suspend () -> Flow<T>,
        onCollect: suspend (T) -> Unit,
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher,
    ) {

        onStart()
        viewModelScope.launch(dispatcher) {
            try {
                block().catch { onError(it) }
                    .collectLatest { result ->
                        onCollect(result)
                    }
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }

    protected fun <T : Any, R : Any> createPagingFlow(
        pagingSourceFactory: () -> PagingSource<Int, T>,
        mapper: (T) -> R,
    ): Flow<PagingData<R>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(mapper) }
            .cachedIn(viewModelScope)
    }

    protected fun emitEffect(effect: E) {
        val now = System.currentTimeMillis()

        val effectType = effect!!::class
        val lastEffectType = lastEffect?.let { it::class }

        if (effectType == lastEffectType && now - lastTime < effectDebounceMs) {
            return
        }

        lastEffect = effect
        lastTime = now

        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }
}