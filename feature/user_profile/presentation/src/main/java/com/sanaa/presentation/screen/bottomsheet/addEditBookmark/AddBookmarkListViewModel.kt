package com.sanaa.presentation.screen.bottomsheet.addEditBookmark

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.screen.bottomsheet.components.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val stringProvider: VodStringProvider
) : BaseViewModel<AddBookmarkListUiState, AddBookmarksEffect>(AddBookmarkListUiState()),
    AddBookmarksInteractionListeners {

    init {
        refreshLists()
    }

    private fun refreshLists() {
        tryToExecute(
            block = { listsStatusProvider.refreshLists() },
            onError = {
                it.printStackTrace()
            }
        )
    }

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
            block = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = { savedList ->
                resetState()
                emitEffect(AddBookmarksEffect.Dismiss)
                updateState {
                    copy(snackBarData = SnackData(message = stringProvider.createListSuccess, isError = false))
                }
                listsStatusProvider.markItemSaved(mediaId)
                listsStatusProvider.addList(
                    SavedList(
                        title = savedList.title,
                        itemCount = savedList.itemCount,
                        id = savedList.id
                    )
                )
                viewModelScope.launch {
                    listsStatusProvider.refreshLists()
                }
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