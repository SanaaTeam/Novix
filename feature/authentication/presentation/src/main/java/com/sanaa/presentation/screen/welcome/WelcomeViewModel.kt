package com.sanaa.presentation.screen.welcome

import com.sanaa.presentation.screen.login.SnackData
import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.IdentityException
import exceptions.NoInternetException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.IdentityStringProvider
import usecase.CreateGuestSessionUseCase
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val createGuestSessionUseCase: CreateGuestSessionUseCase,
    private val stringProvider: IdentityStringProvider,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<WelcomeScreenUiState, WelcomeScreenEffects>(
    initialState = WelcomeScreenUiState(),
    defaultDispatcher = dispatcher
), WelcomeScreenInteractionListener {

    override fun onLoginClicked() = emitEffect(WelcomeScreenEffects.NavigateToLogin)

    override fun onContinueClicked() {
        tryToExecute(
            block = { createGuestSessionUseCase.createGuestSession() },
            onSuccess = { emitEffect(WelcomeScreenEffects.ReturnGuestResultCode) },
            onError = { e -> onContinueClickedFailed(e) }
        )
    }

    private fun onContinueClickedFailed(e: IdentityException) {
        val errorMessage = when (e) {
            is NoInternetException -> stringProvider.noInternetConnectionError
            else -> stringProvider.somethingWentWrongError
        }
        updateState { copy(snackBarData = SnackData(message = errorMessage, isError = true)) }
    }

    override fun onExit() = emitEffect(WelcomeScreenEffects.ExitApp)

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }
}