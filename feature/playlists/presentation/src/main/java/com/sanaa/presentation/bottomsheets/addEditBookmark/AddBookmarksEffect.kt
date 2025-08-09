package com.sanaa.presentation.bottomsheets.addEditBookmark

sealed interface AddBookmarksEffect {
    object AddSuccess : AddBookmarksEffect
    object AddFailure : AddBookmarksEffect
}