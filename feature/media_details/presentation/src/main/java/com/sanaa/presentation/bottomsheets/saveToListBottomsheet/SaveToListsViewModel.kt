package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.details_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListsViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SaveToListsUiState, SaveToListEffects>(SaveToListsUiState(), dispatcher) {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onSuccess = ::onLoadPlaylistsSuccess,
            onError = this@SaveToListsViewModel.onErrorAccrue()
        )
    }

    private fun onLoadPlaylistsSuccess(domainLists: List<SavedList>) {
        val uiLists = domainLists.map { savedList ->
            PlaylistUiItems(
                id = savedList.id.toLong(),
                title = savedList.title,
                itemCount = savedList.itemCount
            )
        }
        updateState { it.copy(isLoading = false, playlists = uiLists) }
    }

    private fun onErrorAccrue(): (Throwable) -> Unit = {
        updateState { it.copy(isLoading = false, errorMessage = "Failed to load lists.") }
    }

    fun onPlaylistSelected(listId: Long) {
        updateState {
            it.copy(
                selectedListId = listId, isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = addMovieToSavedList(selectedListId, mediaId),
            onSuccess = onAddMovieToSavedListSuccess(mediaId),
            onError = ::onErrorAccrue
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
        updateState { it.copy(isLoading = false) }
        savedListsStatusProvider.markItemSaved(mediaId.toInt())
        loadPlaylists()
        emitEffect(SaveToListEffects.AddedSuccessfully)
    }

    private fun onErrorAccrue(throwable: Throwable) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SaveToListEffects.FailedToAdd)
    }
}