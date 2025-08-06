package com.sanaa.presentation.screen.myAccount

data class MyAccountScreenUiState(
    val currentUser: UserUiState = UserUiState(),
    val showChangeLanguageBottomSheet: Boolean = false,
    val showContentRestrictionBottomSheet: Boolean = false,
    val showChangeThemeBottomSheet: Boolean = false,
    val selectedLanguage: String? = null,
    val selectedContentRestriction: ContentRestrictionUiState? = null,
    val selectedTheme: ThemeUiState? = null,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = true
)


enum class ThemeUiState {
    LIGHT,
    DARK
}

enum class ContentRestrictionUiState {
    RESTRICTED,
    UNRESTRICTED,
    MODERATE_RESTRICTION
}