package com.sanaa.presentation.screen.playlist

interface PlayListScreenInteractionListener: PlayListItemsInteractionListener {
    fun onNavigateToLogin()
    fun onRetryLoadSavedLists()
}
interface PlayListItemsInteractionListener{
    fun onFabBottomSheetClicked()
    fun onDismissAddBottomSheet()
    fun onNavigateToSavedDetails(listId: Int, title: String)

}