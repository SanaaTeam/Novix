package com.sanaa.presentation.screen.myRating

interface MyRatingScreenInteractionListener {
    fun onBackClick()
    fun onTabSelected(tab: MyRatingTab)
    fun onDeleteIconClick(mediaId: Int, mediaType: MediaTypeUi)
    fun onRetryLoadDetails()
    fun onMediaClick(id: Int, mediaType: MediaTypeUi)
    fun onDismissSnack()
    fun onShowSuccessSnackBar(message: String)
    fun onShowErrorSnackBar(message: String)
}