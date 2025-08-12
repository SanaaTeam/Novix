package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.details_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListsViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SaveToListsUiState, SaveToListEffects>(SaveToListsUiState(),dispatcher),
    SaveToListsInteractionListener {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onSuccess = { domainLists ->
                val uiLists = domainLists.map { savedList ->
                    PlaylistUiItems(
                        id = savedList.id.toLong(),
                        title = savedList.title,
                        itemCount = savedList.itemCount
                    )
                }
                updateState { it.copy(isLoading = false, playlists = uiLists) }
            },
            onError = {
                updateState { it.copy(isLoading = false, errorMessage = "Failed to load lists.") }
            }
        )
    }

    override fun onPlaylistSelected(listId: Long) {
        updateState {
            it.copy(
                selectedListId = listId, isAddButtonEnabled = true
            )
        }
    }

    override fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = {
                manageSavedListItemsUseCase.addMovieToSavedList(
                    listId = selectedListId.toInt(),
                    movieId = mediaId.toInt()
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                savedListsStatusProvider.markItemSaved(mediaId.toInt())
                loadPlaylists()
                emitEffect(SaveToListEffects.AddedSuccessfully)
            },
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add item to list."
                    )
                }
                emitEffect(SaveToListEffects.FailedToAdd)
            }
        )
    }
}