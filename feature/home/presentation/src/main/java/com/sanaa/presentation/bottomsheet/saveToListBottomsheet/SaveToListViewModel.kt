package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState(), dispatcher),
    SaveToListInteractionListener {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            callee = { listsStatusProvider.savedLists },
            onCollect = { playlist ->
                updateState {
                    it.copy(
                        playlists = playlist.map {
                            PlaylistUiItem(
                                title = it.title,
                                itemCount = it.itemCount,
                                id = it.id.toLong()
                            )
                        }
                    )
                }
            },
        )
    }

    override fun onPlaylistSelected(listId: Long) {
        updateState {
            it.copy(
                selectedListId = listId,
                isAddButtonEnabled = true
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
                listsStatusProvider.markItemSaved(mediaId.toInt())
                viewModelScope.launch {
                    listsStatusProvider.refreshLists()
                }
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