package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.mediaListContent.getMediaStrategy.GetTopRatedMedia
import com.sanaa.presentation.model.MediaItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MediaListSectionViewModel(
    private val mediaProvider: GetTopRatedMedia,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MediaListSectionUiState, MediaListContentEffect>(
    MediaListSectionUiState(),
    dispatcher
), MediaListSectionInteractionListener {

    init {
        fetchTitle()
        fetchMedia()
    }

    private fun fetchTitle(){
        val title = mediaProvider.getMediaSectionTitle()
        updateState {
            it.copy(title = title)
        }
    }
    private fun fetchMedia(genreId: Int? = null) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true, selectedGenreId = genreId)
                }
                val mediaList = mediaProvider.getMediaList(1, state.value.selectedGenreId)
                updateState {
                    it.copy(mediaList = mediaList,)
                }
            }, onSuccess = {
                updateState {
                    it.copy(isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        )
    }

    override fun onGenreClick(id: Int?) {
        if(id != state.value.selectedGenreId) {
            fetchMedia(id)
        }
    }

    override fun onMediaClick(media: MediaItem) {
        emitEffect(MediaListContentEffect.NavigateToMediaDetails(media.id, media.mediaType))
    }
}