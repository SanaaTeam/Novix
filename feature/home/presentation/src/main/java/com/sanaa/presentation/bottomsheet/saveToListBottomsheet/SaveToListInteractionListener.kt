package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

interface SaveToListInteractionListener {
    fun onPlaylistSelected(listId: Long)
    fun onAddClicked(mediaId: Long)
    fun onSnackBarDismiss()
}