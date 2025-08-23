package com.sanaa.tvapp.presentation.screens.login

import com.sanaa.tvapp.state.SnackData

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
    val snackBarData: SnackData? = null,
)
