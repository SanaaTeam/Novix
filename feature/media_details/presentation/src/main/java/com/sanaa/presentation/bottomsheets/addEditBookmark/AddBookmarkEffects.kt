package com.sanaa.presentation.bottomsheets.addEditBookmark

sealed interface AddBookmarkEffects {
    object AddSuccess : AddBookmarkEffects
    object AddFailure : AddBookmarkEffects
}