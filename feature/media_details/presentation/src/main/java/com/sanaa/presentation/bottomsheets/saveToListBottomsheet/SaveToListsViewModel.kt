package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.details_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListsViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SaveToListsUiState, SaveToListEffects>(SaveToListsUiState(), dispatcher) {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.getSavedListsOnce() },
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
        updateState { copy(isLoading = false, playlists = uiLists) }
    }

    private fun onErrorAccrue(): (Throwable) -> Unit = {
        updateState { copy(isLoading = false, errorMessage = "Failed to load lists.") }
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
            onSuccess = { onAddMovieToSavedListSuccess(mediaId) },
            onError = ::onErrorAccrue
        )
    }

    private fun addMovieToSavedList(
        selectedListId: Long,
        mediaId: Long,
    ): suspend () -> Unit = {
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
    }

    private fun onAddMovieToSavedListSuccess(mediaId: Long): (Boolean) -> Unit = {
        updateState { copy(isLoading = false) }
        loadPlaylists()
        emitEffect(SaveToListEffects.AddedSuccessfully)
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SaveToListEffects.FailedToAdd)
    }
}