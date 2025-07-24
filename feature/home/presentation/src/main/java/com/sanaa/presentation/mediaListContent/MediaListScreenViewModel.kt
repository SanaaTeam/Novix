package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.mediaListContent.getMediaStrategy.GetTopRatedMedia
import com.sanaa.presentation.mediaListContent.getMediaStrategy.MediaProvider
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

class MediaListScreenViewModel(
    private val mediaType: MediaType,
    private val mediaProvider: MediaProvider,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageMovieUseCase: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MediaListScreenUiState, MediaListScreenEffect>(
    MediaListScreenUiState(),
    dispatcher
), MediaListScreenInteractionListener {

    init {
        fetchTitle()
        fetchGenres()
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

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                mediaProvider.getMediaGenreList()
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(genreList = genres, isLoading = false)
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
        emitEffect(MediaListScreenEffect.NavigateToMediaDetails(media.id, media.mediaType))
    }

    override fun onSaveIconClick(media: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        emitEffect(MediaListScreenEffect.NavigateBack)
    }
}