package com.sanaa.presentation.screen.playlist

interface PlayListScreenInteractionListener {
    fun onFabBottomSheetClicked()
    fun onButtonLoginClicked()
    fun onDismissAddBottomSheet()
    fun onItemListClicked(listId: Int, title: String)
}