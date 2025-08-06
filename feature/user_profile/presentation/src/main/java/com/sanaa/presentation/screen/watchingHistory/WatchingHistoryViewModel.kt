package com.sanaa.presentation.screen.watchingHistory

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toMediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import usecase.GetLoggedInUserUseCase
import usecase.history.ManageWatchingHistoryUseCase
import usecase.search.search_param.MediaType

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor(
    private val manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : BaseViewModel<WatchingHistoryUiState, WatchingHistoryScreenEffect>
    (initialState = WatchingHistoryUiState(), defaultDispatcher = dispatcher),
    WatchingHistoryInteractionListener {

    init {
        loadWatchingHistory()
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi?) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }
        loadWatchingHistoryByType(mediaTypeUi)
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(WatchingHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(mediaItem: MediaItemUiModel) {
        // TODO: Implement save/unsave functionality
    }

    override fun onBackClick() {
        emitEffect(WatchingHistoryScreenEffect.NavigateBack)
    }

    private fun loadWatchingHistory() {
        tryToExecute(
            callee = {
                val user = getLoggedInUserUseCase.getLoggedInUser()
                manageWatchingHistoryUseCase.getWatchingHistory(user.username, null)
            },
            onSuccess = { historyFlow ->
                val pagingFlow = historyFlow.map { mediaHistoryItems ->
                    mediaHistoryItems.map {
                        it.toMediaItemUiModel()
                    }.let { PagingData.from(it) }
                }
                updateState {
                    it.copy(
                        watchingHistory = pagingFlow.cachedIn(viewModelScope),
                        error = null
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        watchingHistory = flowOf(PagingData.empty()),
                        error = exception.message
                    )
                }
            }
        )
    }

    private fun loadWatchingHistoryByType(mediaTypeUi: MediaTypeUi?) {
        tryToExecute(
            callee = {
                val user = getLoggedInUserUseCase.getLoggedInUser()
                val mediaType = when (mediaTypeUi) {
                    MediaTypeUi.MOVIE -> MediaType.MOVIE
                    MediaTypeUi.TV_SHOW -> MediaType.TV_SERIES
                    null -> null
                }
                manageWatchingHistoryUseCase.getWatchingHistory(user.username, mediaType)
            },
            onSuccess = { historyFlow ->
                val pagingFlow = historyFlow.map { mediaHistoryItems ->
                    mediaHistoryItems.map {
                        it.toMediaItemUiModel()
                    }.let { PagingData.from(it) }
                }
                updateState {
                    it.copy(
                        watchingHistory = pagingFlow.cachedIn(viewModelScope),
                        error = null
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        watchingHistory = flowOf(PagingData.empty()),
                        error = exception.message
                    )
                }
            }
        )
    }
}
