package com.sanaa.presentation.bottomsheet.addEditBookmark

import com.sanaa.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
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
        tryToCollect(
            block = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onCollect = {
                tryToExecute(
                    block = {
                        manageSavedListItemsUseCase.addMovieToSavedList(
                            listId = 0,
                            movieId = mediaId
                        )
                    },
                    onSuccess = {
                        updateState { copy(isLoading = false) }
                        emitEffect(AddBookmarkEffect.AddSuccess)
                    },
                )
            },
            onError = ::onErrorAccrue
        )
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