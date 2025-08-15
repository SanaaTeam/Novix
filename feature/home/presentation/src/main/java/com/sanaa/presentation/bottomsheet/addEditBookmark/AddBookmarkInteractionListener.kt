package com.sanaa.presentation.bottomsheet.addEditBookmark

interface AddBookmarkInteractionListener {
    fun onListTitleChanged(title: String)
    fun resetState()
    fun onAddClicked(mediaId: Int)
    fun onSnackBarDismiss()
}