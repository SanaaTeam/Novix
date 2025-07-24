package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.model.MediaItem

interface MediaListScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(media: MediaItem)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
}