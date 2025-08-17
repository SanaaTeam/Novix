package com.sanaa.presentation.model

import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import entity.Movie

fun Movie.toUiModel() = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
)

