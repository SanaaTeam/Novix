package com.sanaa.presentation.filter_bottomsheet


interface FilterBottomSheetInteractionsListener {
    fun onClearFilters()
    fun onRatingChanged(newRating: Int)
    fun onGenreSelected(genre: String)
    fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>)
}