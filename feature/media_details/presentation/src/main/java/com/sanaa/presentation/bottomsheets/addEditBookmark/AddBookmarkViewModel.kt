package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.details_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarkViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<AddBookmarkUiState, AddBookmarkEffects>(AddBookmarkUiState(), dispatcher) {

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
                emitEffect(AddBookmarkEffects.AddSuccess)
                savedListsStatusProvider.markItemSaved(mediaId)
            },
            onError = ::onErrorAccrue
        )
    }

    private fun onErrorAccrue(throwable: Throwable) {
        updateState {
            emitEffect(AddBookmarkEffects.AddFailure)
            it.copy(
                isLoading = false,
                errorMessage = "Failed to create list. Please try again."
            )
        }
    }
}