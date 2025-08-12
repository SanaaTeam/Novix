package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

interface SaveToListsInteractionListener {
    fun onPlaylistSelected(listId: Long)
    fun onAddClicked(mediaId: Long)
}