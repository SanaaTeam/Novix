package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.state.MediaItem

data class MyAccountScreenUiState(
    val currentUser: UserUiState = UserUiState(),
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
    val watchingHistoryTvShows: List<MediaItem> = emptyList(),
    val myRatingTvShows: List<MediaItem> = emptyList(),
) {
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



