package com.sanaa.presentation.screen.playlist

interface PlayListScreenInteractionListener: PlayListItemsInteractionListener {
    fun onNavigateToLogin()
    fun onRetryLoadSavedLists()
    fun onSnackBarDismiss()
}
interface PlayListItemsInteractionListener{
    fun onAddNewListClicked()
    fun onDismissAddBottomSheet()
    fun onNavigateToSavedDetails(listId: Int, title: String)
}