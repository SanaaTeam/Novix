package com.sanaa.presentation.screen.myRating

sealed interface MyRatingScreenEffect {
    object NavigateBack : MyRatingScreenEffect
    data class NavigateToMediaDetails(val mediaId: Int, val mediaTypeUi: MediaTypeUi): MyRatingScreenEffect

}
