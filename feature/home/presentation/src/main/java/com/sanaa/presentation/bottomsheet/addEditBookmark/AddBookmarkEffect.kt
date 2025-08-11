package com.sanaa.presentation.bottomsheet.addEditBookmark

sealed interface AddBookmarkEffect {
    object AddSuccess : AddBookmarkEffect
    object AddFailure : AddBookmarkEffect
}