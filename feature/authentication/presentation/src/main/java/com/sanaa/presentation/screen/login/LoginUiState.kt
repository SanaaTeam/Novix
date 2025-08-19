package com.sanaa.presentation.screen.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
    val snackBarData: SnackData? = null
)

data class SnackData(
    val message: String,
    val isError: Boolean
)
