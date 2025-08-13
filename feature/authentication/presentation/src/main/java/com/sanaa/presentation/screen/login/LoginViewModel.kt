package com.sanaa.presentation.screen.login

import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.InvalidUserOrPasswordException
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.IdentityStringProvider
import usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val identityStringProvider: IdentityStringProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<LoginUiState, LoginScreenEffects>(LoginUiState(), ioDispatcher),
    LoginScreenInteractionListener {

    override fun onUsernameChanged(newUsername: String) {
        updateStateAndRefreshSubmit { copy(username = newUsername, usernameError = null) }
    }

    override fun onPasswordChanged(newPassword: String) {
        updateStateAndRefreshSubmit { copy(password = newPassword, passwordError = null) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onLoginClicked() {
        val current = state.value
        if (!current.canSubmit) return

        updateState { copy(isLoading = true, canSubmit = false) }

        tryToExecute(
            callee = login(),
            onSuccess = onLoginSuccess(),
            onError = ::onDataLoadError
        )
    }

    private fun login(): suspend () -> Unit = {
        val userName = state.value.username
        val password = state.value.password
        if (userName.isNotBlank() && password.isNotBlank()) {
            loginUseCase.login(userName, password)
        } else
            throw Exception(identityStringProvider.enterUserNameAndPasswordError)
    }

    private fun onLoginSuccess(): (Unit) -> Unit = {
        updateState {
            val updated = copy(isLoading = false)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
        emitEffect(LoginScreenEffects.ReturnLoggedInResultCode)
    }

    fun onDataLoadError(throwable: Throwable) {
        updateState {
            val updated = copy(isLoading = false)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
        val message = when (throwable) {
            is InvalidUserOrPasswordException -> identityStringProvider.invalidUserNameAndPasswordError
            is NoNetworkException -> identityStringProvider.noInternetConnectionError
            else -> identityStringProvider.somethingWentWrongError
        }
        emitEffect(LoginScreenEffects.ShowError(message = message))
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

    private fun updateStateAndRefreshSubmit(transform: LoginUiState.() -> LoginUiState) {
        updateState {
            val updated = transform(this)
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