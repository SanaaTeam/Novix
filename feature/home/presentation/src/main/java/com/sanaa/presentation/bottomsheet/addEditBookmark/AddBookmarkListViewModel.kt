package com.sanaa.presentation.bottomsheet.addEditBookmark

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<AddBookmarkListUiState, AddBookmarkEffect>(AddBookmarkListUiState(), dispatcher) {

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
            onSuccess = onAddBookmarkListSuccess(mediaId),
            onError = ::onErrorAccrue
        )
    }

    private fun onAddBookmarkListSuccess(mediaId: Int): (SavedList) -> Unit = {
        resetState()
        emitEffect(AddBookmarkEffect.AddSuccess)
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = "Failed to create list. Please try again."
            )
        }
        emitEffect(AddBookmarkEffect.AddFailure)
    }
}