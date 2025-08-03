package com.sanaa.presentation.screen.myRating

interface MyRatingScreenInteractionListener {
    fun onBackClick()
    fun onTabSelected(tab: MyRatingTab)
    fun onDeleteIconClick(mediaId: Int, mediaType: String)
    fun onRetryLoadDetails()
//    fun onMediaClick(id: Int, mediaType: String)
}