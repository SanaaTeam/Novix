package com.sanaa.presentation.details_base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E>(
    initialState: T,
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state: MutableStateFlow<T> by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<T> by lazy { _state.asStateFlow() }

    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    protected fun updateState(updater: (T) -> T) {
        _state.update(updater)
    }

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onError: (exception: Throwable) -> Unit = {},
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

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
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

    protected fun <T : Any, R : Any> createPagingFlow(
        pagingSourceFactory: () -> PagingSource<Int, T>,
        mapper: (T) -> R
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


    companion object {
        private const val PAGE_SIZE = 20
    }
}