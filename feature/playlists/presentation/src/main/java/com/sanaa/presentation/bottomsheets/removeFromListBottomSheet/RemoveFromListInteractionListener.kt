package com.sanaa.presentation.bottomsheets.removeFromListBottomSheet

interface RemoveFromListInteractionListener {
    fun onPlaylistClick(listId: Int)
    fun onRemoveClick()
    fun onSnackBarDismiss()
    fun onBottomSheetDismiss()
}