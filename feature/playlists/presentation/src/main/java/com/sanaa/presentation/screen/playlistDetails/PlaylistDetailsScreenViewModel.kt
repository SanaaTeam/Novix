package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.model.toUiModel
import com.sanaa.presentation.savedBase.BasePagingSource
import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import repository.SavedMovieStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseViewModel<SavedDetailsScreenUiState, PlaylistDetailsScreenEffect>(SavedDetailsScreenUiState()),
    PlaylistDetailsInteractionListener {
    private val listId: Int = checkNotNull(savedStateHandle["listId"]) {
        "listId is required in SavedStateHandle"
    }

    private val title: String = checkNotNull(savedStateHandle["title"]) {
        "title is required in SavedStateHandle"
    }

    init {
        loadItemsInSaved(listId)
        updateState { it.copy(listId = listId) }
    }

    private fun loadItemsInSaved(listId: Int) {
        tryToExecute(
            callee = {
                val moviesPagingFlow = createPagingFlow(
                    pagingSourceFactory = { createSavedMoviesPagingSource(listId) },
                    mapper = Movie::toUiModel
                )
                moviesPagingFlow.combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
                    pagingData.map { mediaItem ->
                        mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
                    }
                }.cachedIn(viewModelScope)
            },
            onSuccess = { moviesFlow ->
                updateState { it.copy(movieList = moviesFlow, isLoading = false) }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    updateState {
                        it.copy(errorMessage = exception.message, isLoading = false)
                    }
                }
            }
        )
    }

    override fun onMediaClick(
        mediaId: Int,
        mediaType: MediaTypeUi
    ) {
        emitEffect(PlaylistDetailsScreenEffect.NavigateToMediaDetails(mediaId, mediaType))
    }

    override fun onSaveIconClick(mediaItem: MediaItem) {
        if (mediaItem.isSaved) {
            savedMovieStatusProvider.markUnsaved(mediaItem.id)

            tryToExecute(
                callee = {
                    manageSavedListItemsUseCase.removeMovieFromSavedList(
                        listId = listId,
                        movieId = mediaItem.id
                    )
                },
                onSuccess = {
                    emitEffect(PlaylistDetailsScreenEffect.RefreshList)
                },
                onError = {
                    savedMovieStatusProvider.markSaved(mediaItem.id)
                }
            )
        }
    }
    override fun onDismissSaveToListBottomSheet() {
        updateState { it.copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { it.copy(showAddListBottomSheet = false) }
    }

    override fun onBackClick() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBack)
    }

    override fun onDeleteListClicked() {
        updateState { it.copy(showBottomSheet = true) }
    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onListDeletedSuccessfully() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBack)
    }

    private fun loadSavedMovies(listId: Int): Flow<PagingData<MediaItem>> {
        val moviesPagingFlow = createPagingFlow(
            pagingSourceFactory = { createSavedMoviesPagingSource(listId) },
            mapper = Movie::toUiModel
        )
        return moviesPagingFlow.combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
            pagingData.map { mediaItem ->
                mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
            }
        }.cachedIn(viewModelScope)
    }


    private fun createSavedMoviesPagingSource(listId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageSavedListItemsUseCase.getAllItemsInSavedList(listId, page)
        }
    }


}
