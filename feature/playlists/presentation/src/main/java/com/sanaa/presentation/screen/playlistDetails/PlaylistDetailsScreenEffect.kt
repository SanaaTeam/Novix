package com.sanaa.presentation.screen.playlistDetails

import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi

sealed interface PlaylistDetailsScreenEffect {
    object NavigateBack : PlaylistDetailsScreenEffect
    object NavigateBackAfterDelete : PlaylistDetailsScreenEffect
    object RefreshList : PlaylistDetailsScreenEffect

    data class NavigateToMediaDetails(val mediaId: Int, val mediaTypeUi: MediaTypeUi) :
        PlaylistDetailsScreenEffect

}