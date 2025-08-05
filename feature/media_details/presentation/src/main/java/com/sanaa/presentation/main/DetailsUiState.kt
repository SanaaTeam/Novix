package com.sanaa.presentation.main


data class DetailsUiState(
    val isDarkTheme: Boolean = true,
    val isReady: Boolean = false,
    val safeContentThreshold: Float = 0f,
)