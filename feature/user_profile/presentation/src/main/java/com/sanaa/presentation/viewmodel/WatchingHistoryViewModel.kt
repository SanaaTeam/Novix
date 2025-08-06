package com.sanaa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.WatchingHistoryUiState
import com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen.ContinueWatchingScreenEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import usecase.history.ManageWatchingHistoryUseCase
import usecase.search.search_param.MediaType
import usecase.GetLoggedInUserUseCase
import exceptions.NoLoggedInUserException
import javax.inject.Inject

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor(
    private val manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WatchingHistoryUiState())
    val state: StateFlow<WatchingHistoryUiState> = _state.asStateFlow()

    private val _effect = MutableStateFlow<ContinueWatchingScreenEffect?>(null)
    val effect: StateFlow<ContinueWatchingScreenEffect?> = _effect.asStateFlow()

    init {
        loadWatchingHistory()
    }

    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi?) {
        _state.value = _state.value.copy(selectedMediaTypeUi = mediaTypeUi)
        loadWatchingHistoryByType(mediaTypeUi)
    }

    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        _effect.value = ContinueWatchingScreenEffect.NavigateToMediaDetails(id, mediaTypeUi)
    }

    fun onSaveIconClick(mediaItem: MediaItem) {
        // TODO: Implement save/unsave functionality
    }

    fun onBackClick() {
        _effect.value = ContinueWatchingScreenEffect.NavigateBack
    }

    private fun loadWatchingHistory() {
        viewModelScope.launch {
            try {
                val user = getLoggedInUserUseCase.getLoggedInUser()
                val watchingHistoryFlow = manageWatchingHistoryUseCase.getWatchingHistory(user.username, null)
                val pagingFlow = watchingHistoryFlow.map { mediaHistoryItems ->
                    val mediaItems = mediaHistoryItems.map { historyItem ->
                        MediaItem(
                            id = historyItem.id,
                            title = "",
                            imageUrl = historyItem.posterImageUrl,
                            mediaTypeUi = when (historyItem.mediaType) {
                                MediaType.MOVIE -> MediaTypeUi.MOVIE
                                MediaType.TV_SERIES -> MediaTypeUi.TV_SHOW
                            },
                            isSaved = historyItem.isSaved
                        )
                    }
                    PagingData.from(mediaItems)
                }
                _state.value = _state.value.copy(watchingHistory = pagingFlow.cachedIn(viewModelScope))
            } catch (e: NoLoggedInUserException) {
                _state.value = _state.value.copy(watchingHistory = flowOf(PagingData.empty<MediaItem>()))
            } catch (e: Exception) {
                _state.value = _state.value.copy(watchingHistory = flowOf(PagingData.empty<MediaItem>()))
            }
        }
    }

    private fun loadWatchingHistoryByType(mediaTypeUi: MediaTypeUi?) {
        viewModelScope.launch {
            try {
                val user = getLoggedInUserUseCase.getLoggedInUser()
                val mediaType = when (mediaTypeUi) {
                    MediaTypeUi.MOVIE -> MediaType.MOVIE
                    MediaTypeUi.TV_SHOW -> MediaType.TV_SERIES
                    null -> null
                }
                
                val watchingHistoryFlow = manageWatchingHistoryUseCase.getWatchingHistory(user.username, mediaType)
                val pagingFlow = watchingHistoryFlow.map { mediaHistoryItems ->
                    val mediaItems = mediaHistoryItems.map { historyItem ->
                        MediaItem(
                            id = historyItem.id,
                            title = "",
                            imageUrl = historyItem.posterImageUrl,
                            mediaTypeUi = when (historyItem.mediaType) {
                                MediaType.MOVIE -> MediaTypeUi.MOVIE
                                MediaType.TV_SERIES -> MediaTypeUi.TV_SHOW
                            },
                            isSaved = historyItem.isSaved
                        )
                    }
                    PagingData.from(mediaItems)
                }
                _state.value = _state.value.copy(watchingHistory = pagingFlow.cachedIn(viewModelScope))
            } catch (e: NoLoggedInUserException) {
                _state.value = _state.value.copy(watchingHistory = flowOf(PagingData.empty<MediaItem>()))
            } catch (e: Exception) {
                _state.value = _state.value.copy(watchingHistory = flowOf(PagingData.empty<MediaItem>()))
            }
        }
    }
}
