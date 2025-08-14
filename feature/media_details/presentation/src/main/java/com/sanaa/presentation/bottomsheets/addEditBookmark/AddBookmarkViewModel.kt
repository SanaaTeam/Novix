package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.details_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookmarkViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarkUiState, AddBookmarkEffects>(AddBookmarkUiState(), dispatcher),
    AddBookmarksInteractionListener {

    init {
        refreshLists()
    }

    private fun refreshLists() {
        tryToExecute(
            callee = { savedListsStatusProvider.refreshLists() },
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
        updateState { copy(listTitle = "", isLoading = false, errorMessage = null) }
    }

    override fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = {
                resetState()
                emitEffect(AddBookmarkEffects.AddSuccess)
                savedListsStatusProvider.markItemSaved(mediaId)
            },
            onError = ::onErrorAccrue
        )
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            emitEffect(AddBookmarkEffects.AddFailure)
            copy(
                isLoading = false,
                errorMessage = "Failed to create list. Please try again."
            )
        }
    }
}