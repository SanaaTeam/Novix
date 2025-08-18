package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

interface SaveToListInteractionListener {
    fun onPlaylistClick(listId: Int)
    fun onAddClick()
    fun onSnackBarDismiss()
    fun onCreateNewListClick()
    fun onRequestBottomSheetDismiss()
}