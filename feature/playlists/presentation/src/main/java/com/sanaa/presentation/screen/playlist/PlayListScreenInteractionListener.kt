package com.sanaa.presentation.screen.playlist

interface PlayListScreenInteractionListener {
    fun onFabBottomSheetClicked()
    fun onNavigateToLogin()
    fun onDismissAddBottomSheet()
    fun onRetryLoadSavedLists()
    fun onNavigateToSavedDetails(listId: Int, title: String)
}