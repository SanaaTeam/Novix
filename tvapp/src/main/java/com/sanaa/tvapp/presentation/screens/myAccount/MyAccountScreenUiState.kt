package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.state.MediaItemUiState

data class MyAccountScreenUiState(
    val currentUser: UserUiState = UserUiState(),
    val selectedLanguage: String? = null,
    val selectedContentRestriction: ContentRestrictionUiState? = null,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val savedLanguage: String? = null,
    val savedContentRestriction: ContentRestrictionUiState? = null,
    val watchingHistoryMovies: List<MediaItemUiState> = emptyList(),
    val myRatingMovies: List<MediaItemUiState> = emptyList(),
    val watchingHistoryTvShows: List<MediaItemUiState> = emptyList(),
    val myRatingTvShows: List<MediaItemUiState> = emptyList(),
    val showLogoutDialog: Boolean = false,
) {
    enum class ContentRestrictionUiState {
        RESTRICTED,
        UNRESTRICTED,
        MODERATE_RESTRICTION
    }

    companion object {
        const val ARABIC_LANGUAGE_CODE = "ar"
        const val ENGLISH_LANGUAGE_CODE = "en"
    }
}



