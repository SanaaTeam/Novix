package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUi

interface HomeScreenInteractionListener {
    fun onMoviesCardClick()
    fun onTvShowsCardClick()
    fun onPeopleCardClick()

    fun onShowAllTopRatingClick()
    fun onShowAllContinueWatchingClick()

    fun onMovieGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItemUiState)
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
    fun onDismissLoginBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onRetryClick()
    fun onSnackBarDismiss()
}