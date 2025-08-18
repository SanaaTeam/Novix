package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

interface SaveToListInteractionsListener {
    fun onPlaylistClick(listId: Int)
    fun onAddClick()
    fun onSnackBarDismiss()
    fun onCreateNewListClick()
    fun onRequestBottomSheetDismiss()
}