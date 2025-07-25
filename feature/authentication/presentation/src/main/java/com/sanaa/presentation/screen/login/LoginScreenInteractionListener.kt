package com.sanaa.presentation.screen.login

interface LoginScreenInteractionListener {
    fun onUsernameChanged(newUsername: String)
    fun onPasswordChanged(newPassword: String)
    fun onTogglePasswordVisibility()
    fun onLoginClicked()
    fun onForgotPasswordClicked()
    fun onCreateAccountClicked()
    fun onBackClicked()
}