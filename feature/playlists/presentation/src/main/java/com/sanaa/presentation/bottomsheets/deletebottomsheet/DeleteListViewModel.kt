package com.sanaa.presentation.bottomsheets.deletebottomsheet

import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlist.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class DeleteListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<DeleteListUiState, DeleteListEffect>(DeleteListUiState(), dispatcher),
    DeleteInteractionListener {

    override fun onDeleteConfirmed(listId: Long) {
        updateState { copy(isLoading = true) }

        tryToExecute(
            callee = { manageSavedListsUseCase.deleteSavedList(listId.toInt()) },
            onSuccess = ::onDeleteConfirmedSuccess,
            onError = ::onDeleteConfirmedFailed,
        )
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDeleteConfirmedSuccess(unit: Unit) {
        updateState { copy(isLoading = false) }
        emitEffect(DeleteListEffect.Dismiss)
    }

    private fun onDeleteConfirmedFailed(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.deleteListFailed, isError = true)
            )
        }
    }
}