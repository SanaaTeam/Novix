package com.sanaa.tvapp.presentation.screens.login

import android.util.Log
import com.sanaa.presentation.screen.welcome.WelcomeScreenEffects
import com.sanaa.tvapp.presentation.screens.searchScreen.base.TvSearchBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.InvalidUserOrPasswordException
import exceptions.NoInternetConnectionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.StringProvider
import usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val stringProvider: StringProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TvSearchBaseViewModel<LoginUiState, LoginScreenEffects>(LoginUiState(), ioDispatcher),
    LoginScreenInteractionListener {

    override fun onUsernameChanged(newUsername: String) {
        updateStateAndRefreshSubmit { it.copy(username = newUsername, usernameError = null) }
    }

    override fun onPasswordChanged(newPassword: String) {
        updateStateAndRefreshSubmit { it.copy(password = newPassword, passwordError = null) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    override fun onLoginClicked() {
        val current = state.value
        if (!current.canSubmit) return

        updateState { it.copy(isLoading = true, canSubmit = false) }

        tryToExecute(
            callee = {
                val userName = state.value.username
                val password = state.value.password
                if (userName.isNotBlank() && password.isNotBlank()) {
                    loginUseCase.login(userName, password)
                } else
                    throw Exception(stringProvider.enterUserNameAndPasswordError)
            },
            onSuccess = {
                updateState { prev ->
                    val updated = prev.copy(isLoading = false)
                    updated.copy(canSubmit = isSubmitAllowed(updated))
                }
                emitEffect(LoginScreenEffects.ShowSuccess(stringProvider.loginSuccess))
            },
            onError = ::onDataLoadError
        )
    }

    fun onDataLoadError(throwable: Throwable) {
        updateState { prev ->
            val updated = prev.copy(isLoading = false)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
        val message = when (throwable) {
            is InvalidUserOrPasswordException -> stringProvider.invalidUserNameAndPasswordError
            is NoInternetConnectionException -> stringProvider.noInternetConnectionError
            else -> stringProvider.somethingWentWrongError
        }
        emitEffect(LoginScreenEffects.ShowError(message = message))
    }

    private fun updateStateAndRefreshSubmit(transform: (LoginUiState) -> LoginUiState) {
        updateState { previous ->
            val updated = transform(previous)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
    }

    override fun onContinueClicked() = emitEffect(LoginScreenEffects.ReturnGuestResultCode)

    private fun isSubmitAllowed(uiState: LoginUiState): Boolean =
        uiState.username.isNotBlank() &&
                uiState.password.isNotBlank() &&
                uiState.usernameError == null &&
                uiState.passwordError == null &&
                !uiState.isLoading
}