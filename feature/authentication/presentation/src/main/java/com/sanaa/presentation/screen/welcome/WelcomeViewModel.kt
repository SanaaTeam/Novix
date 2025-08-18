package com.sanaa.presentation.screen.welcome

import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CreateGuestSessionUseCase
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val createGuestSessionUseCase: CreateGuestSessionUseCase,
     val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<Unit, WelcomeScreenEffects>(initialState = Unit, defaultDispatcher = dispatcher),
    WelcomeScreenInteractionListener {

    override fun onLoginClicked() = emitEffect(WelcomeScreenEffects.NavigateToLogin)

    override fun onContinueClicked()  {
        tryToExecute(
            block = { createGuestSessionUseCase.createGuestSession() },
            onSuccess = { emitEffect(WelcomeScreenEffects.ReturnGuestResultCode) }
        )
    }

    override fun onExit() = emitEffect(WelcomeScreenEffects.ExitApp)
}