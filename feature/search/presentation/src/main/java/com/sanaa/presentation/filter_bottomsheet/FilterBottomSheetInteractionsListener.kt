package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState

interface FilterBottomSheetInteractionsListener {
    fun onApplyClicked(tabIndex: Int)
    fun onClearFilters(tabIndex: Int)
    fun onRatingChanged(tabIndex: Int,newRating: Int)
    fun onGenreSelected(tabIndex: Int,genre: GenreUiState)
    fun onYearRangeChanged(tabIndex: Int,newRange: ClosedFloatingPointRange<Float>)
}