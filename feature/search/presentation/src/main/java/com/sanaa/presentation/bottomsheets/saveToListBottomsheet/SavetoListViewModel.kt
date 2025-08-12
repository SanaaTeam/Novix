package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SavetoListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SaveToListUiState, SavetoListEffect>(SaveToListUiState(), dispatcher) {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onSuccess = ::onLoadPlaylistsSuccess,
            onError = ::onErrorAccrue
        )
    }

    private fun onLoadPlaylistsSuccess(domainLists: List<SavedList>) {
        val uiLists = domainLists.map { savedList ->
            PlaylistUiItem(
                id = savedList.id.toLong(),
                title = savedList.title,
                itemCount = savedList.itemCount
            )
        }
        updateState { copy(isLoading = false, playlists = uiLists) }
    }

    fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(
                selectedListId = listId, isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = addMovieToSavedList(selectedListId, mediaId),
            onSuccess = onAddMovieToSavedListSuccess(mediaId),
            onError = ::onAddMovieToSavedListFailed,
        )
    }

    private fun addMovieToSavedList(
        selectedListId: Long,
        mediaId: Long,
    ): suspend () -> Boolean = {
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
    }

    private fun onAddMovieToSavedListSuccess(mediaId: Long): (Boolean) -> Unit = {
        updateState { copy(isLoading = false) }
        savedListsStatusProvider.markItemSaved(mediaId.toInt())
        loadPlaylists()
        emitEffect(SavetoListEffect.AddedSuccessfully)
    }

    private fun onAddMovieToSavedListFailed(throwable: Throwable) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SavetoListEffect.FailedToAdd)
    }

    private fun onErrorAccrue(throwable: Throwable) {
        updateState { copy(isLoading = false, errorMessage = "Failed to load lists.") }
    }
}