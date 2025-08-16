package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

interface SaveToListInteractionsListener {
    fun onPlaylistSelected(listId: Long)
    fun onAddClicked(mediaId: Long)
    fun onSnackBarDismiss()
}