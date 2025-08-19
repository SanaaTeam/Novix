package com.sanaa.presentation.screen.bottomsheet.addEditBookmark

import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.screen.bottomsheet.components.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider
) : BaseViewModel<AddBookmarkListUiState, AddBookmarksEffect>(AddBookmarkListUiState()),
    AddBookmarksInteractionListeners {

    override fun onListTitleChanged(title: String) {
        updateState {
            copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank() && !state.value.isLoading
            )
        }
    }

    override fun resetState() {
        updateState { copy(listTitle = "", isLoading = false) }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onAddClicked() {
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            block = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = ::onAddBookmarkListSuccess,
            onError = ::onErrorAccrue
        )
    }
    private fun onAddBookmarkListSuccess(unit: Unit) {
        resetState()
        emitEffect(AddBookmarksEffect.Dismiss)
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.createListSuccess,
                    isError = false
                )
            )
        }
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(
                    message = stringProvider.createListFailed,
                    isError = true
                )
            )
        }
    }
}