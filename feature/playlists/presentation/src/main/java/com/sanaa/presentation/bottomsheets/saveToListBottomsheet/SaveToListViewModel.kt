package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedMovieStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SavetoListUiState, SaveToListEffect>(SavetoListUiState(),dispatcher) {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onSuccess = { domainLists ->
                val uiLists = domainLists.map { savedList ->
                    PlayListUiItem(
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
            callee = {
                manageSavedListItemsUseCase.addMovieToSavedList(
                    listId = selectedListId.toInt(),
                    movieId = mediaId.toInt()
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                savedMovieStatusProvider.markSaved(mediaId.toInt())
                loadPlaylists()
                emitEffect(SaveToListEffect.AddedSuccessfully)
            },
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add item to list."
                    )
                }
                emitEffect(SaveToListEffect.FailedToAdd)
            }
        )
    }
}