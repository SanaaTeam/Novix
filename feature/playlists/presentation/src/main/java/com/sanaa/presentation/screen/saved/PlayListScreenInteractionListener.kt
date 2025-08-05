package com.sanaa.presentation.screen.saved

interface PlayListScreenInteractionListener {
    fun onFabBottomSheetClicked()
    fun onButtonLoginClicked()
    fun onDismissAddBottomSheet()
    fun onItemListClicked(listId: Int)
}