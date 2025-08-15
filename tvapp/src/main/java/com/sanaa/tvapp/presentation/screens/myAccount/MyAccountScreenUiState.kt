package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.state.MediaItem

data class MyAccountScreenUiState(
    val currentUser: UserUiState = UserUiState(),
    val showChangeLanguageBottomSheet: Boolean = false,
    val showContentRestrictionBottomSheet: Boolean = false,
    val showChangeThemeBottomSheet: Boolean = false,
    val selectedLanguage: String? = null,
    val selectedContentRestriction: ContentRestrictionUiState? = null,
    val selectedTheme: ThemeUiState? = null,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val savedLanguage: String? = null,
    val savedTheme: ThemeUiState? = null,
    val savedContentRestriction: ContentRestrictionUiState? = null,
    val watchingHistoryMovies: List<MediaItem> = emptyList(),
    val myRatingMovies: List<MediaItem> = emptyList(),
) {
    companion object {
        const val ARABIC_LANGUAGE_CODE = "ar"
        const val ENGLISH_LANGUAGE_CODE = "en"
    }

    enum class ThemeUiState {
        LIGHT,
        DARK
    }

    enum class ContentRestrictionUiState {
        RESTRICTED,
        UNRESTRICTED,
        MODERATE_RESTRICTION
    }

}



