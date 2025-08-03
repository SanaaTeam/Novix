package com.sanaa.presentation.screen.myRating

sealed interface MyRatingScreenEffect {
    object NavigateBack : MyRatingScreenEffect
    data class ShowMessage(val message: String) : MyRatingScreenEffect
    data class NavigateToMediaDetails(val mediaId: Int, val mediaType: String) : MyRatingScreenEffect

}
