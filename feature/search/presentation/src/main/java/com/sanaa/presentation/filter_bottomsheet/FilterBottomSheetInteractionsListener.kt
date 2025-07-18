package com.sanaa.presentation.filter_bottomsheet

import entity.Genre

interface FilterBottomSheetInteractionsListener {
    fun onApplyClicked()
    fun onClearFilters()
    fun onRatingChanged(newRating: Int)
    fun onGenreSelected(genre: String)
    fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>)
}