package com.sanaa.presentation.screen.playlistDetails

sealed interface PlaylistDetailsScreenEffect {
    object NavigateBack : PlaylistDetailsScreenEffect
    data object ShowErrorSnackBar : PlaylistDetailsScreenEffect
    data object ShowSuccessSnackBar : PlaylistDetailsScreenEffect
}