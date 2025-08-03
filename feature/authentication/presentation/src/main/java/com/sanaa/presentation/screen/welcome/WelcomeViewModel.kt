package com.sanaa.presentation.screen.welcome

import android.util.Log
import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import usecase.CreateGuestSessionUseCase
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val createGuestSessionUseCase: CreateGuestSessionUseCase
) : BaseViewModel<Unit, WelcomeScreenEffects>(Unit),
    WelcomeScreenInteractionListener {

    override fun onLoginClicked() = emitEffect(WelcomeScreenEffects.NavigateToLogin)

    override fun onContinueClicked()  {
        tryToExecute(
            callee = {
                createGuestSessionUseCase.createGuestSession()
            },
            onSuccess = {
                Log.d("test99", "onContinueClicked:success ")
                emitEffect(WelcomeScreenEffects.ReturnGuestResultCode)
            },
            onError = {
                Log.d("test99", "onContinueClicked: Error= :${it.message} ")
            }
        )
    }

    override fun onExit() = emitEffect(WelcomeScreenEffects.ExitApp)
}