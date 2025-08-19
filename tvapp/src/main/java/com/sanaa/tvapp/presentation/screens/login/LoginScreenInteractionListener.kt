package com.sanaa.tvapp.presentation.screens.login

interface LoginScreenInteractionListener {
    fun onUsernameChanged(newUsername: String)
    fun onPasswordChanged(newPassword: String)
    fun onTogglePasswordVisibility()
    fun onLoginClicked()
    fun onContinueClicked()
}