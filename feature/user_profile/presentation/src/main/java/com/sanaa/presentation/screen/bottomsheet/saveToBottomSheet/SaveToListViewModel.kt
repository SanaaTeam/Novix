package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.profileBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState()) {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            block = { listsStatusProvider.savedLists },
            onCollect = { playlist ->
                updateState {
                    copy(
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

    fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(
                selectedListId = listId,
                isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            block = {
                manageSavedListItemsUseCase.addMovieToSavedList(
                    listId = selectedListId.toInt(),
                    movieId = mediaId.toInt()
                )
            },
            onSuccess = {
                updateState { copy(isLoading = false) }
                listsStatusProvider.markItemSaved(mediaId.toInt())
                viewModelScope.launch {
                    listsStatusProvider.refreshLists()
                }
                emitEffect(SaveToListEffect.AddedSuccessfully)
            },
            onError = {
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = "Failed to add item to list."
                    )
                }
                emitEffect(SaveToListEffect.FailedToAdd)
            }
        )
    }
}