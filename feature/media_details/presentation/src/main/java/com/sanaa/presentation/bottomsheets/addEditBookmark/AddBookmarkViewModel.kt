package com.sanaa.presentation.bottomsheets.addEditBookmark

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AddBookmarkUiState, AddBookmarkEffects>(AddBookmarkUiState(), dispatcher),
    AddBookmarksInteractionListener {

    init {
//        refreshLists()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
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

    override fun onAddClicked(mediaId: Int) {
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true) }
        val currentTitle = state.value.listTitle.trim()
        tryToExecute(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onSuccess = ::onAddBookmarkListSuccess,
            onError = ::onErrorAccrue
        )
    }

    private fun onAddBookmarkListSuccess(unit: Unit): (SavedList) -> Unit = { savedList ->
        resetState()
        emitEffect(AddBookmarkEffects.Dismiss)
        updateState {
            copy(snackBarData = SnackData(message = stringProvider.createListSuccess, isError = false))
        }
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.createListFailed, isError = true)
            )
        }
    }
}