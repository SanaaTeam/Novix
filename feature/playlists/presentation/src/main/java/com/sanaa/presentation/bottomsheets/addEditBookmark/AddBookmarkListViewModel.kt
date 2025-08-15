package com.sanaa.presentation.bottomsheets.addEditBookmark

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlist.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<AddBookmarkListUiState, AddBookMarksEffect>(
    AddBookmarkListUiState(),
    dispatcher
), AddBookMarksInteractionsListener {


    init {
        refreshLists()
    }

    private fun refreshLists() {
        tryToExecute(
            callee = { listsStatusProvider.refreshLists() },
            onError = {
                it.printStackTrace()
            }
        )
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
        tryToCollect(
            callee = { manageSavedListsUseCase.createSavedList(currentTitle) },
            onCollect = onAddBookmarkListSuccess(mediaId),
            onError = ::onErrorAccrue
        )
    }

    private fun onAddBookmarkListSuccess(mediaId: Int): (SavedList) -> Unit = {
        resetState()
        emitEffect(AddBookMarksEffect.Dismiss)
        updateState {
            copy(snackBarData = SnackData(message = stringProvider.createListSuccess, isError = false))
        }
        listsStatusProvider.markItemSaved(mediaId)
        listsStatusProvider.addList(
            SavedList(
                title = it.title,
                itemCount = it.itemCount,
                id = it.id,
                itemsIds = emptyList()
            )
        )
        viewModelScope.launch {
            listsStatusProvider.refreshLists()
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