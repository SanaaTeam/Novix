package com.sanaa.tvapp.presentation.screens.login

import com.sanaa.tvapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.InvalidUserOrPasswordException
import exceptions.NoInternetException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.IdentityStringProvider
import usecase.CreateGuestSessionUseCase
import usecase.LoginUseCase
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val createGuestSessionUseCase: CreateGuestSessionUseCase,
    private val identityStringProvider: IdentityStringProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<LoginUiState, LoginScreenEffects>(LoginUiState(), ioDispatcher),
    LoginScreenInteractionListener {

    override fun onUsernameChanged(newUsername: String) {
        updateStateAndRefreshSubmit { it.copy(username = newUsername, usernameError = null) }
    }

    override fun onPasswordChanged(newPassword: String) {
        updateStateAndRefreshSubmit { it.copy(password = newPassword, passwordError = null) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onLoginClicked() {
        val current = state.value
        if (!current.canSubmit) return

        updateState { copy(isLoading = true, canSubmit = false) }

        tryToExecute(
            block = {
                val userName = state.value.username
                val password = state.value.password
                if (userName.isNotBlank() && password.isNotBlank()) {
                    loginUseCase.login(userName, password)
                } else {
                    throw Exception(identityStringProvider.enterUserNameAndPasswordError)
                }
            },
            onSuccess = {
                updateState {
                    val updated = copy(isLoading = false)
                    updated.copy(canSubmit = isSubmitAllowed(updated))
                }
                emitEffect(LoginScreenEffects.ReturnGuestResultCode)
            },
            onError = ::onDataLoadError
        )
    }


    fun onDataLoadError(throwable: Throwable) {
        updateState {
            val updated = copy(isLoading = false)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
        val message = when (throwable) {
            is InvalidUserOrPasswordException -> identityStringProvider.invalidUserNameAndPasswordError
            is NoInternetException -> identityStringProvider.noInternetConnectionError
            else -> identityStringProvider.somethingWentWrongError
        }
        emitEffect(LoginScreenEffects.ShowError(message = message))
    }


    private fun updateStateAndRefreshSubmit(transform: (LoginUiState) -> LoginUiState) {
        updateState {
            val updated = transform(this)
            updated.copy(canSubmit = isSubmitAllowed(updated))
        }
    }


    override fun onContinueClicked() {
        tryToExecute(
            block = {
                createGuestSessionUseCase.createGuestSession()
            },
            onSuccess = {
                emitEffect(LoginScreenEffects.ReturnGuestResultCode)
            },
            onError = ::onDataLoadError
        )
    }

    private fun isSubmitAllowed(uiState: LoginUiState): Boolean =
        uiState.username.isNotBlank() &&
                uiState.password.isNotBlank() &&
                uiState.usernameError == null &&
                uiState.passwordError == null &&
                !uiState.isLoading
}