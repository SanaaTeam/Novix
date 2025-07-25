package com.sanaa.presentation.screen.login

interface LoginScreenInteractionListener {
    fun onUsernameChanged(value: String)
    fun onPasswordChanged(value: String)
    fun onTogglePasswordVisibility()
    fun onLoginClicked()
    fun onForgotPasswordClicked()
    fun onCreateAccountClicked()
    fun onBackClicked()
}