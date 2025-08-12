package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarksListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarksListUiState, Unit>(AddBookmarksListUiState(),dispatcher) {

    fun onListTitleChanged(title: String) {
        updateState {
            copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
            )
        }
    }

    fun resetState() {
        updateState { copy(listTitle = "", isLoading = false, errorMessage = null) }
    }

    fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = {
                resetState()
                emitEffect(Unit)
                savedListsStatusProvider.markItemSaved(mediaId)
            },
            onError = {
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = "Failed to create list. Please try again."
                    )
                }
            }
        )
    }
}