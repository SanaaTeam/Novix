package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

interface SaveToListInteractionListener {
    fun onPlaylistClick(listId: Long)
    fun onAddClick()
    fun onSnackBarDismiss()
    fun onCreateNewListClick()
    fun onRequestBottomSheetDismiss()
}