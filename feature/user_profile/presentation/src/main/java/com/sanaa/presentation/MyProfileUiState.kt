package com.sanaa.presentation

data class MyProfileUiState(
    val isDarkTheme: Boolean = true,
    val isReady: Boolean = false,
    val safeContentThreshold: Float = 0f,
)