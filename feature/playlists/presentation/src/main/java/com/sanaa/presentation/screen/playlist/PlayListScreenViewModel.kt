package com.sanaa.presentation.screen.playlist

import android.util.Log
import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
) :
    BaseViewModel<PlayListScreenUiState, PlayListScreenEffect>(PlayListScreenUiState()),
    PlayListScreenInteractionListener {

    init {
        loadSavedLists()
        getUserState()
    }

    fun loadSavedLists() = tryToExecute(
        dispatcher = Dispatchers.IO,
        callee = {
            val savedLists = manageSavedListsUseCase.getSavedLists().map {
                it.toUiModel()
            }
            updateState { it.copy(isLoading = false, lists = savedLists) }
        },
        onError = { err ->
            updateState { it.copy(isLoading = false, errorMessage = err.message) }
        }
    )

    fun onListAdded() {
        loadSavedLists()
        emitEffect(PlayListScreenEffect.ShowSuccessToAddListSnackBar)
    }

    fun onListAddFailed() {
        emitEffect(PlayListScreenEffect.ShowErrorToAddListSnackBar)
    }

    fun onListDeletedSuccessfully() {
        loadSavedLists()
        emitEffect(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)
        Log.i("PlaylistScreen", "onListDeletedSuccessfully: ")
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

    override fun onItemListClicked(listId: Int, title: String) {
        emitEffect(PlayListScreenEffect.NavigateToSavedDetails(listId, title))
    }

    private fun getUserState() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = { isUserLoggedIn ->
                updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
            }
        )

    }


}