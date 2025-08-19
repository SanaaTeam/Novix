package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

interface MovieDetailsScreenInteractionListener {
    fun onWatchTrailerClick(urlString: String)
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onRetryLoadDetails()
    fun onRateMovieClick()
    fun onRatingChange(rating:Int)
    fun onDismissRateDialog()
    fun onSummitRateClick()
}