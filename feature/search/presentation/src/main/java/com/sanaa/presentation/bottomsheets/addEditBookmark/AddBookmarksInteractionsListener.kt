package com.sanaa.presentation.bottomsheets.addEditBookmark

interface AddBookmarksInteractionsListener {
    fun onListTitleChanged(title: String)
    fun resetState()
    fun onAddClicked(mediaId: Int)
    fun onSnackBarDismiss()
}