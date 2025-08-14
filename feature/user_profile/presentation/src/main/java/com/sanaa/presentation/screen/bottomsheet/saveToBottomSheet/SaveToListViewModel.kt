package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.profileBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState()) {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {

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