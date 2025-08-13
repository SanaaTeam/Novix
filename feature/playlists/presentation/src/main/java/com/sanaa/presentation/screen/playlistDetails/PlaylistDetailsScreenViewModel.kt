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
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) :
    BaseViewModel<SavedDetailsScreenUiState, PlaylistDetailsScreenEffect>(SavedDetailsScreenUiState(),dispatcher),
    PlaylistDetailsInteractionListener {
    private val listId: Int = checkNotNull(savedStateHandle["listId"]) {
        "listId is required in SavedStateHandle"
    }

    init {
        loadItemsInSaved(listId)
        updateState { copy(listId = listId) }
    }

    private fun loadItemsInSaved(listId: Int) {
        updateState { copy(isLoading = true) }
        tryToCollect(
            callee = {
                loadSavedMovies(listId)
            },
            onCollect = { movies ->
                updateState {
                    copy(movieList = flowOf(movies))
                }
                updateState { copy(isLoading = false) }

            },
            onError = ::onDataLoadError
        )
    }

    override fun onMediaClick(
        mediaId: Int,
        mediaType: MediaTypeUi,
    ) {
        emitEffect(PlaylistDetailsScreenEffect.NavigateToMediaDetails(mediaId, mediaType))
    }

    override fun onSaveIconClick(mediaItem: MediaItem) {
        tryToExecute(
            callee = {
                manageSavedListItemsUseCase.removeMovieFromSavedList(
                    listId = listId,
                    movieId = mediaItem.id
                )
                savedListsStatusProvider.refreshLists()
            },
            onSuccess = {
                savedListsStatusProvider.markItemUnsaved(mediaItem.id)
                loadItemsInSaved(listId)
                emitEffect(PlaylistDetailsScreenEffect.ShowSuccessSnackBar)
            },
            onError = ::onDataLoadError

        )
    }

    override fun onBackClick() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBack)
    }

    override fun onDeleteListClicked() {
        updateState { copy(showBottomSheet = true) }
    }

    override fun onDismissBottomSheet() {
        updateState { copy(showBottomSheet = false) }
    }

    override fun onListDeletedSuccessfully() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBackAfterDelete)
    }

    private fun loadSavedMovies(listId: Int): Flow<PagingData<MediaItem>> {
        return  createPagingFlow(
            pagingSourceFactory = { createSavedMoviesPagingSource(listId) },
            mapper = Movie::toUiModel
        ).combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
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

    internal fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { copy(isLoading = false, errorMessage = null) }
        } else {
            updateState { copy(isLoading = false, errorMessage = e.message) }
            emitEffect(PlaylistDetailsScreenEffect.ShowErrorSnackBar)

        }
    }
}
