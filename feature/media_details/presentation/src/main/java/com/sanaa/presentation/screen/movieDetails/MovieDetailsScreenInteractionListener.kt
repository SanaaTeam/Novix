package com.sanaa.presentation.screen.movieDetails

import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel

interface MovieDetailsScreenInteractionListener {
    fun onBackClick()
    fun onWatchTrailerClick()
    fun onReadMoreClick()
    fun onBookmarkClick(movie: MovieUiModel)
    fun onSimilarMovieClick(movieId: Int)
    fun onRateMovieClick()
    fun onDismissLoginBottomSheet()
    fun onLoginButtonClick()
    fun onActorCardClick(actorId: Int)
    fun onShowReviewsClick(movieId: Int)
    fun onGenreClicked(genre: GenreUiModel)
    fun onRetryLoadDetails()
    fun onRatingChanged(newRating: Int)
    fun onDismissRateBottomSheet()
    fun onSubmitRateBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onDismissSnack()
    fun onShowSuccessSnackBar(message: String)
    fun onShowErrorSnackBar(message: String)
}