package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState

interface FilterBottomSheetInteractionsListener {
    fun onApplyClicked()
    fun onClearFilters()
    fun onRatingChanged(newRating: Int)
    fun onGenreSelected(genre: GenreUiState)
    fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>)
}