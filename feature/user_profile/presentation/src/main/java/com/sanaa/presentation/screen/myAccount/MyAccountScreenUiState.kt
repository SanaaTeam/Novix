package com.sanaa.presentation.screen.myAccount

data class MyAccountScreenUiState(
    val username: String = "",
    val showChangeLanguageBottomSheet: Boolean = false,
    val showContentRestrictionBottomSheet: Boolean = false,
    val showChangeThemeBottomSheet: Boolean = false,
    val selectedLanguage: String? = null,
)