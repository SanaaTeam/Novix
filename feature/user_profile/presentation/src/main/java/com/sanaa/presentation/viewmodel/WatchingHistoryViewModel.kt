package com.sanaa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.WatchingHistoryUiState
import com.sanaa.presentation.state.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor() : ViewModel(), MediaTabScreenInteractionListener {

    private val _state = MutableStateFlow(
        WatchingHistoryUiState(
            watchingHistory = createMockPagingData(),
            selectedMediaTypeUi = MediaTypeUi.MOVIE
        )
    )
    val state: StateFlow<WatchingHistoryUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MediaTabScreenEffect>()
    val effect: Flow<MediaTabScreenEffect> = _effect

    private fun createMockPagingData(): Flow<PagingData<MediaItem>> {
        val mockData = listOf(
            MediaItem(
                id = 1,
                title = "Movie 1",
                imageUrl = "https://example.com/movie1.jpg",
                mediaTypeUi = MediaTypeUi.MOVIE,
                rating = "8.5"
            ),
            MediaItem(
                id = 2,
                title = "Movie 2",
                imageUrl = "https://example.com/movie2.jpg",
                mediaTypeUi = MediaTypeUi.MOVIE,
                rating = "7.8"
            ),
            MediaItem(
                id = 3,
                title = "TV Show 1",
                imageUrl = "https://example.com/series1.jpg",
                mediaTypeUi = MediaTypeUi.TV_SHOW,
                rating = "9.2"
            ),
            MediaItem(
                id = 4,
                title = "Movie 3",
                imageUrl = "https://example.com/movie3.jpg",
                mediaTypeUi = MediaTypeUi.MOVIE,
                rating = "8.0"
            ),
            MediaItem(
                id = 5,
                title = "TV Show 2",
                imageUrl = "https://example.com/series2.jpg",
                mediaTypeUi = MediaTypeUi.TV_SHOW,
                rating = "8.7"
            )
        )
        return flowOf(PagingData.from(mockData)).cachedIn(viewModelScope)
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        _state.value = _state.value.copy(selectedMediaTypeUi = mediaTypeUi)
    }

    override fun onMovieGenreClick(id: Int?) {
        // Handle movie genre click
    }

    override fun onTvShowGenreClick(id: Int?) {
        // Handle TV show genre click
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        viewModelScope.launch {
            _effect.emit(MediaTabScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
        }
    }

    override fun onSaveIconClick(media: MediaItem) {
        // Handle save icon click
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _effect.emit(MediaTabScreenEffect.NavigateBack)
        }
    }
}
