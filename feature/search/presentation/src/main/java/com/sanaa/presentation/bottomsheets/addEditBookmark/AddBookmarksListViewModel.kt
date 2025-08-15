package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.screen.componants.SnackData
import com.sanaa.presentation.searchBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarksListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarksListUiState, AddBookmarksEffect>(AddBookmarksListUiState(), dispatcher),
    AddBookmarksInteractionsListener {

    override fun onListTitleChanged(title: String) {
        updateState {
            copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
            )
        }
    }

    override fun resetState() {
        updateState { copy(listTitle = "", isLoading = false) }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = {
                resetState()
                emitEffect(AddBookmarksEffect.Dismiss)
                updateState {
                    copy(snackBarData = SnackData(message = stringProvider.createListSuccess, isError = false))
                }
                savedListsStatusProvider.markItemSaved(mediaId)
            },
            onError = {
                updateState {
                    copy(
                        isLoading = false,
                        snackBarData = SnackData(message = stringProvider.createListFailed, isError = true)
                    )
                }
            }
        )
    }
}