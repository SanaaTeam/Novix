package com.sanaa.presentation.screen.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
)

data class SnackData(
    val message: String,
    val isError: Boolean
)
