package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.state.MediaItem

data class MyAccountScreenUiState(
    val currentUser: UserUiState = UserUiState(),
    val selectedLanguage: String? = null,
    val selectedContentRestriction: ContentRestrictionUiState? = null,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val savedLanguage: String? = null,
    val savedContentRestriction: ContentRestrictionUiState? = null,
    val watchingHistoryMovies: List<MediaItem> = emptyList(),
    val myRatingMovies: List<MediaItem> = emptyList(),
    val watchingHistoryTvShows: List<MediaItem> = emptyList(),
    val myRatingTvShows: List<MediaItem> = emptyList(),
) {
    enum class ContentRestrictionUiState {
        RESTRICTED,
        UNRESTRICTED,
        MODERATE_RESTRICTION
    }
}



