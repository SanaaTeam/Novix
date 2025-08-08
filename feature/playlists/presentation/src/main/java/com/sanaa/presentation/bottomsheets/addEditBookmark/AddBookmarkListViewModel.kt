package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedMovieStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

) : BaseViewModel<AddBookmarkListUiState, Unit>(AddBookmarkListUiState(), dispatcher) {

    fun onListTitleChanged(title: String) {
        updateState {
            it.copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
            )
        }
    }

    fun resetState() {
        updateState { it.copy(listTitle = "", isLoading = false, errorMessage = null) }
    }

    fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = {
                resetState()
                emitEffect(Unit)
                savedMovieStatusProvider.markSaved(mediaId)
            },
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to create list. Please try again."
                    )
                }
            }
        )
    }
}