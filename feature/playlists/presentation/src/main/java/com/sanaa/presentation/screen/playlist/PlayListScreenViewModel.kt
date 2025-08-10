package com.sanaa.presentation.screen.playlist

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import repository.SavedListsStatusProvider
import usecase.CheckIfUserIsLoggedInUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) :
    BaseViewModel<PlayListScreenUiState, PlayListScreenEffect>(
        PlayListScreenUiState(),
        defaultDispatcher = dispatcher
    ),
    PlayListScreenInteractionListener {

    init {
        loadSavedLists()
    }

    fun loadSavedLists() =
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = { isUserLoggedIn ->
                updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
                if (isUserLoggedIn) {
                    tryToCollect(
                        dispatcher = Dispatchers.IO,
                        callee = { listsStatusProvider.savedLists },
                        onCollect = { savedLists ->
                            updateState {
                                it.copy(isLoading = false, lists = savedLists.map {
                                    it.toUiModel()
                                })
                            }
                        },
                        onError = { e ->
                            if (e is NoNetworkException) {
                                updateState { it.copy(isLoading = false, noInternetConnection = true) }
                            } else {
                                updateState { it.copy(isLoading = false, errorMessage = e.message) }
                            }
                        }
                    )
                }
            }
        )


    fun onListAdded() {
        refreshLists()
        emitEffect(PlayListScreenEffect.ShowSuccessToAddListSnackBar)
    }

    fun onListAddFailed() {
        emitEffect(PlayListScreenEffect.ShowErrorToAddListSnackBar)
    }

    fun onListDeletedSuccessfully() {
        refreshLists()
        emitEffect(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)
    }


    override fun onFabBottomSheetClicked() {
        updateState { it.copy(showAddBottomSheet = true) }
    }

    override fun onButtonLoginClicked() {
        emitEffect(PlayListScreenEffect.NavigateToLogin)
    }


    override fun onDismissAddBottomSheet() {
        updateState { it.copy(showAddBottomSheet = false) }
    }

    override fun onRetryLoadSavedLists() {
        loadSavedLists()
    }

    override fun onItemListClicked(listId: Int, title: String) {
        emitEffect(PlayListScreenEffect.NavigateToSavedDetails(listId, title))
    }


    private fun refreshLists() {
        tryToExecute(
            callee = { listsStatusProvider.refreshLists() },
            onSuccess = {
                loadSavedLists()
            }
        )
    }
}