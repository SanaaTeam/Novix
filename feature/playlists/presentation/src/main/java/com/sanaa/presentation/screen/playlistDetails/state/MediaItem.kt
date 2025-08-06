package com.sanaa.presentation.screen.playlistDetails.state

data class MediaItem(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val isSaved: Boolean? = null,
)


