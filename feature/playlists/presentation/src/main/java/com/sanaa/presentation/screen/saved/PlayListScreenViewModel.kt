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
    PlayListScreenInteractionListener
{
    init {
        updateUserStatus()
    }
    override fun onFabBottomSheetClicked() {
        TODO("Not yet implemented")
    }

    override fun onButtonLoginClicked() {
        TODO("Not yet implemented")
    }

    override fun onDismissAddBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun onAddNewListClicked() {
        TODO("Not yet implemented")
    }
    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }
    private fun updateUserStatus(){
        tryToExecute(callee = ::getUserState)
    }
}