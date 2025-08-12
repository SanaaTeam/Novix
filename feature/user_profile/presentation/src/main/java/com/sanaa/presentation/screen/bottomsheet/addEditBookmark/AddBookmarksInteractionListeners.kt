package com.sanaa.presentation.screen.bottomsheet.addEditBookmark

interface AddBookmarksInteractionListeners {
    fun onListTitleChanged(title: String)
    fun resetState()
    fun onAddClicked(mediaId: Int)
}