package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.screen.componants.SnackData
import com.sanaa.presentation.searchBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarksListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarksListUiState, AddBookmarksEffect>(AddBookmarksListUiState(), dispatcher),
    AddBookmarksInteractionsListener {

    override fun onListTitleChanged(title: String) {
        updateState {
            copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank() && !state.value.isLoading
            )
        }
    }

    override fun resetState() {
        updateState { copy(listTitle = "") }
    }

    override fun onAddClicked() {
        if (!state.value.isAddButtonEnabled) return

        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = { onAddBookmarkListSuccess() },
            onError = { onErrorAccrue() }
        )
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onAddBookmarkListSuccess() {
        resetState()
        emitEffect(AddBookmarksEffect.Dismiss)
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.createListSuccess, isError = false)
            )
        }
    }

    private fun onErrorAccrue() {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.createListFailed, isError = true)
            )
        }
    }
}