package com.sanaa.tvapp.presentation.screens.login

sealed interface LoginScreenEffects {
    data class ShowError(val message: String) : LoginScreenEffects
    data class ShowSuccess(val message: String) : LoginScreenEffects
    object ReturnGuestResultCode : LoginScreenEffects
}