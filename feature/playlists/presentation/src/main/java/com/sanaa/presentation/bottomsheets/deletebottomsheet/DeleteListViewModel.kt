package com.sanaa.presentation.bottomsheets.deletebottomsheet

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class DeleteListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<DeleteListUiState, DeleteListEffect>(DeleteListUiState(),dispatcher) {

    fun onDeleteConfirmed(listId: Long) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = { manageSavedListsUseCase.deleteSavedList(listId.toInt()) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                emitEffect(DeleteListEffect.DeleteSuccess)
            },
            onError = { e ->
                emitEffect(DeleteListEffect.DeleteFailure)
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