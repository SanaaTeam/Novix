package com.sanaa.presentation.model

import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import entity.Movie

fun Movie.toUiModel(
    isSaved: Boolean = false,
): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        isSaved = isSaved,
        imageUrl = posterImageUrl
    )
}
