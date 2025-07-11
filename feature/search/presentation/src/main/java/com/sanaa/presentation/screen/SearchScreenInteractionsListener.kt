package com.sanaa.presentation.screen

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onCancelRecentSearchItemClicked()
    fun onSaveIconClicked()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked()
}