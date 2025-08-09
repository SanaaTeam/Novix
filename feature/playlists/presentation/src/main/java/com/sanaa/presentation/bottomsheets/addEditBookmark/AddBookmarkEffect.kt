package com.sanaa.presentation.bottomsheets.addEditBookmark

sealed interface AddBookmarkEffect {
    object AddSuccess : AddBookmarkEffect
    object AddFailure : AddBookmarkEffect
}