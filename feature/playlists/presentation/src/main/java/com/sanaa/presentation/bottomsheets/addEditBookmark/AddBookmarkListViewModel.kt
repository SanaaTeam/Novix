package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlist.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<AddBookmarkListUiState, AddBookMarksEffect>(
    AddBookmarkListUiState(),
    dispatcher
), AddBookMarksInteractionsListener {

    override fun onListTitleChanged(title: String) {
        updateState {
            copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
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
        emitEffect(AddBookMarksEffect.Dismiss)
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