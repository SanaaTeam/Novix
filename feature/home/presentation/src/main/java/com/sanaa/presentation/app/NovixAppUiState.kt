package com.sanaa.presentation.app


data class NovixAppUiState(
    val isDarkTheme: Boolean = false,
    val contentRestriction: ContentRestrictionUiState = ContentRestrictionUiState.RESTRICTED
)

enum class ContentRestrictionUiState {
    RESTRICTED,
    UNRESTRICTED,
    MODERATE_RESTRICTION
}