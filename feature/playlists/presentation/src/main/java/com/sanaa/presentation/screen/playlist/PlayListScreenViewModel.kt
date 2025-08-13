package com.sanaa.presentation.screen.playlist

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
                updateState { copy(isUserLoggedIn = isUserLoggedIn) }
                if (isUserLoggedIn) {
                    tryToCollect(
                        dispatcher = Dispatchers.IO,
                        callee = { listsStatusProvider.savedLists },
                        onCollect = { savedLists ->
                            updateState {
                                copy(isLoading = false, lists = savedLists.map {
                                    it.toUiModel()
                                })
                            }
                        },
                        onError = { err ->
                            updateState { copy(isLoading = false, errorMessage = err.message) }
                        }
                    )
                }
            }
        )

    fun onListDeletedSuccessfully() {
        refreshLists()
        emitEffect(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)
    }


    override fun onFabBottomSheetClicked() {
        updateState { copy(showAddBottomSheet = true) }
    }

    override fun onButtonLoginClicked() {
        emitEffect(PlayListScreenEffect.NavigateToLogin)
    }


    override fun onDismissAddBottomSheet() {
        updateState { copy(showAddBottomSheet = false) }
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