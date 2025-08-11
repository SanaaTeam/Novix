package com.sanaa.presentation.screen.playlistDetails

import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi

sealed interface PlaylistDetailsScreenEffect {
    object NavigateBack : PlaylistDetailsScreenEffect
    data object ShowErrorSnackBar : PlaylistDetailsScreenEffect
    data object ShowSuccessSnackBar : PlaylistDetailsScreenEffect
    object NavigateBackAfterDelete : PlaylistDetailsScreenEffect

    data class NavigateToMediaDetails(val mediaId: Int, val mediaTypeUi: MediaTypeUi) :
        PlaylistDetailsScreenEffect

}