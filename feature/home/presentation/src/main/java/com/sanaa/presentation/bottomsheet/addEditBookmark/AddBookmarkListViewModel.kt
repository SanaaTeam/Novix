package com.sanaa.presentation.bottomsheet.addEditBookmark

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.BaseViewModel
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<AddBookmarkListUiState, AddBookmarkEffect>(AddBookmarkListUiState(), dispatcher) {
    init {
        viewModelScope.launch {
            listsStatusProvider.refreshLists()
        }
    }

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
            onSuccess = onAddBookmarkListSuccess(mediaId),
            onError = onErrorAccrue()
        )
    }

    private fun onAddBookmarkListSuccess(mediaId: Int): (SavedList) -> Unit = {
        resetState()
        emitEffect(AddBookmarkEffect.AddSuccess)
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
    }

    private fun onErrorAccrue(): (Throwable) -> Unit = {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "Failed to create list. Please try again."
            )
        }
        emitEffect(AddBookmarkEffect.AddFailure)
    }
}