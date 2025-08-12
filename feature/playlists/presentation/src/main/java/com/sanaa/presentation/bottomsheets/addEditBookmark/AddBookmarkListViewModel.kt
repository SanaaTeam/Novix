package com.sanaa.presentation.bottomsheets.addEditBookmark

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarkListUiState, AddBookmarksEffect>(
    AddBookmarkListUiState(),
    dispatcher
), AddBookmarksInteractionsListener {


    init {
        viewModelScope.launch {
            listsStatusProvider.refreshLists()
        }
    }

    override fun onListTitleChanged(title: String) {
        updateState {
            it.copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
            )
        }
    }

    override fun resetState() {
        updateState { it.copy(listTitle = "", isLoading = false, errorMessage = null) }
    }

    override fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = {
                resetState()
                emitEffect(AddBookmarksEffect.AddSuccess)
                listsStatusProvider.markItemSaved(mediaId)
                listsStatusProvider.addList(
                    SavedList(
                        title = it.title,
                        itemCount = it.itemCount,
                        id = it.id
                    )
                )
                viewModelScope.launch {
                    listsStatusProvider.refreshLists()
                }
            },
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to create list. Please try again."
                    )
                }
                emitEffect(AddBookmarksEffect.AddFailure)
            }
        )
    }
}