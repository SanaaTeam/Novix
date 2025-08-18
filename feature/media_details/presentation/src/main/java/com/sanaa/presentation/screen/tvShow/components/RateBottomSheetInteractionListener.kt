package com.sanaa.presentation.screen.tvShow.components

interface RateBottomSheetInteractionListener {
    fun onSubmitButtonClick()
    fun onRatingChanged(newRating: Int)
    fun onDismiss()
}