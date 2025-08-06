package com.sanaa.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen.ContinueWatchingScreenEffect

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.WatchingHistoryUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.history.ManageWatchingHistoryUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor(
    private val manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase
) : BaseViewModel<WatchingHistoryUiState, ContinueWatchingScreenEffect>(
    initialState = WatchingHistoryUiState(
        watchingHistory = flowOf(PagingData.empty<MediaItem>())
    )
) {

    init {
        loadWatchingHistory()
    }

    private fun loadWatchingHistory() {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                val loggedInUser = getLoggedInUserUseCase.getLoggedInUser()
                manageWatchingHistoryUseCase.getWatchingHistory(loggedInUser!!.username, null)
            },
            onSuccess = { watchingHistoryFlow ->
                tryToCollect(
                    callee = { watchingHistoryFlow },
                    onCollect = { watchingHistoryList ->
                        val mediaItems = watchingHistoryList.map { it.toState() }
                        val pagingData = PagingData.from(mediaItems)
                        updateState {
                            it.copy(
                                watchingHistory = flowOf(pagingData).cachedIn(viewModelScope),
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onError = { exception ->
                        updateState {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to load watching history"
                            )
                        }
                    }
                )
            },

        )
    }

    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi?) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }

        val mediaType = when (mediaTypeUi) {
            null -> null // null means all types
            MediaTypeUi.MOVIE -> MediaType.MOVIE
            MediaTypeUi.TV_SHOW -> MediaType.TV_SERIES
        }

        loadWatchingHistoryByType(mediaType)
    }

    private fun loadWatchingHistoryByType(mediaType: MediaType?) {
        updateState { it.copy(isLoading = true, error = null) }

        tryToExecute(
            callee = {
                val loggedInUser = getLoggedInUserUseCase.getLoggedInUser()
                manageWatchingHistoryUseCase.getWatchingHistory(loggedInUser!!.username, mediaType)
            },
            onSuccess = { watchingHistoryFlow ->
                tryToCollect(
                    callee = { watchingHistoryFlow },
                    onCollect = { watchingHistoryList ->
                        val mediaItems = watchingHistoryList.map { it.toState() }
                        val pagingData = PagingData.from(mediaItems)
                        updateState {
                            it.copy(
                                watchingHistory = flowOf(pagingData).cachedIn(viewModelScope),
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onError = { exception ->
                        updateState {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to load watching history"
                            )
                        }
                    }
                )
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load watching history"
                    )
                }
            }
        )
    }

    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        viewModelScope.launch {
            emitEffect(ContinueWatchingScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
        }
    }

    fun onSaveIconClick(media: MediaItem) {
        // Handle save icon click
    }


    fun onBackClick() {
        viewModelScope.launch {
            emitEffect(ContinueWatchingScreenEffect.NavigateBack)
        }
    }
}
