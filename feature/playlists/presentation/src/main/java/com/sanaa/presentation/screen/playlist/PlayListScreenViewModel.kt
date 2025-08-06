package com.sanaa.presentation.screen.playlist

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseViewModel<PlayListScreenUiState, PlayListScreenEffect>(PlayListScreenUiState()),
    PlayListScreenInteractionListener {
    init {
        loadSavedLists()
        updateUserStatus()
    }

    private fun loadSavedLists() = tryToExecute(
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
        emitEffect(PlayListScreenEffect.ShowSuccessSnackBar)
    }

    fun onListAddFailed() {
        emitEffect(PlayListScreenEffect.ShowErrorSnackBar)
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
        emitEffect(PlayListScreenEffect.NavigateToSavedDetails(listId,title))
    }

    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

    fun updateUserStatus() {
        tryToExecute(callee = ::getUserState)
    }
}