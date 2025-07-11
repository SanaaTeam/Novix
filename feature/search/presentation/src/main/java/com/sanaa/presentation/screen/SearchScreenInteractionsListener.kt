package com.sanaa.presentation.screen

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onCancelClicked()
    fun onSaveIconClicked()
    fun onTabSelected(index: Int)
}