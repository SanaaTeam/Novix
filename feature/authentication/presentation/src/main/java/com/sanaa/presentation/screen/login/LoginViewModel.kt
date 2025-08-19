package com.sanaa.presentation.screen.login

import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.InvalidUserOrPasswordException
import exceptions.NoInternetException
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
        updateStateAndRefreshSubmit { copy(username = newUsername) }
    }

    override fun onPasswordChanged(newPassword: String) {
        updateStateAndRefreshSubmit { copy(password = newPassword) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onForgotPasswordClicked() {
        emitEffect(LoginScreenEffects.NavigateToForgotPassword)
    }

    override fun onCreateAccountClicked() {
        emitEffect(LoginScreenEffects.NavigateToCreateAccount)
    }
    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }
    override fun onBackClicked() {
        emitEffect(LoginScreenEffects.NavigateBack)
    }

    override fun onLoginClicked() {
        if (!state.value.canSubmit) return

        updateState { copy(isLoading = true, canSubmit = false) }
        tryToExecute(
            block = login(),
            onSuccess = onLoginSuccess(),
            onError = ::onDataLoadError
        )
    }

    private fun login(): suspend () -> Unit = {
        if (state.value.username.isNotBlank()
            && state.value.password.isNotBlank()
        ) {
            loginUseCase.login(state.value.username, state.value.password)
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

    private fun onDataLoadError(throwable: Throwable) {
        updateState {
            val updated = copy(isLoading = false)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
        val message = when (throwable) {
            is InvalidUserOrPasswordException -> identityStringProvider.invalidUserNameAndPasswordError
            is NoInternetException -> identityStringProvider.noInternetConnectionError
            else -> identityStringProvider.somethingWentWrongError
        }
        updateState {
            copy(snackBarData = SnackData(message, isError = true))
        }
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
                !uiState.isLoading
}