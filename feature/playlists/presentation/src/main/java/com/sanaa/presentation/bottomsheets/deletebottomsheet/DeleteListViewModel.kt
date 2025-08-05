package com.sanaa.presentation.bottomsheets.deletebottomsheet

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class DeleteListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase
) : BaseViewModel<DeleteListUiState, Unit>(DeleteListUiState()) {

    fun onDeleteConfirmed(listId: Long) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.deleteSavedList(listId.toInt()) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                emitEffect(Unit)
            },
            onError = { e ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to delete list. Please try again."
                    )
                }
            }
        )
    }
}