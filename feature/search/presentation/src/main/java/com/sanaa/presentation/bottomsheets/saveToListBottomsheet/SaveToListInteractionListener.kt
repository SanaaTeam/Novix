package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

interface SaveToListInteractionListener {
    fun onPlaylistSelected(listId: Long)
    fun onAddClicked(mediaId: Long)
    fun onSnackBarDismiss()
}