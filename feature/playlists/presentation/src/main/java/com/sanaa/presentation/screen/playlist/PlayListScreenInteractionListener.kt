package com.sanaa.presentation.screen.playlist

interface PlayListScreenInteractionListener {
    fun onFabBottomSheetClicked()
    fun onButtonLoginClicked()
    fun onDismissAddBottomSheet()
    fun onRetryLoadSavedLists()
    fun onItemListClicked(listId: Int, title: String)
}