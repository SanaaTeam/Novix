package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

interface SaveToListBottomSheetInteractionListener {
    fun onPlaylistClick(listId: Int)
    fun onAddClick()
    fun onSnackBarDismiss()
    fun onCreateNewListClick()
    fun onRequestBottomSheetDismiss()
}