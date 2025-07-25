package com.sanaa.presentation.screen.login

import com.sanaa.presentation.screen.login_base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class LoginViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<LoginUiState, LoginScreenEffects>(LoginUiState(), ioDispatcher),
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
                // TODO: replace stub with real login call
                delay(800)
            },
            onSuccess = {
                updateState { prev ->
                    val updated = prev.copy(isLoading = false)
                    updated.copy(canSubmit = isSubmitAllowed(updated))
                }
                emitEffect(LoginScreenEffects.NavigateToHome)
                emitEffect(LoginScreenEffects.ShowSuccess(message = "Welcome back!"))
            },
            onError = { throwable ->
                updateState { prev ->
                    val updated = prev.copy(isLoading = false)
                    updated.copy(canSubmit = isSubmitAllowed(updated))
                }
                emitEffect(LoginScreenEffects.ShowError(message = throwable.message.orEmpty()))
            }
        )
    }

    override fun onForgotPasswordClicked() {
        emitEffect(LoginScreenEffects.NavigateToForgotPassword)
    }

    override fun onCreateAccountClicked() {
        emitEffect(LoginScreenEffects.NavigateToCreateAccount)
    }

    override fun onBackClicked() {
        emitEffect(LoginScreenEffects.NavigateBack)
    }


    private fun updateStateAndRefreshSubmit(
        transform: (LoginUiState) -> LoginUiState
    ) {
        updateState { previous ->
            val updated = transform(previous)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
    }

    private fun isSubmitAllowed(uiState: LoginUiState): Boolean =
        uiState.username.isNotBlank() &&
                uiState.password.isNotBlank() &&
                uiState.usernameError == null &&
                uiState.passwordError == null &&
                !uiState.isLoading
}