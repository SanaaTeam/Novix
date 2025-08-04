package com.sanaa.presentation.screen.saved

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseViewModel<PlayListScreenUiState, PlayListScreenEffect>(PlayListScreenUiState()),
    PlayListScreenInteractionListener {
    init {
        updateUserStatus()
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

    override fun onItemListClicked() {
        TODO("Not yet implemented")
    }

    override fun onTitleChange() {
        TODO("Not yet implemented")
    }

    override fun onSavedClicked() {
        TODO("Not yet implemented")
    }

    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

    fun updateUserStatus() {
        tryToExecute(callee = ::getUserState)
    }
}