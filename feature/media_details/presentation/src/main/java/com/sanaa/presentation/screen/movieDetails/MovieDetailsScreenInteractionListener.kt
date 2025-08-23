package com.sanaa.presentation.screen.movieDetails

import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel

interface MovieDetailsScreenInteractionListener :
    MovieDetailsTopBarListener,
    MoreLikeThisListener {
    fun onWatchTrailerClick()
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
    fun onSnackDismissRequested()
}

interface MovieDetailsTopBarListener {
    fun onBackClick()
    fun onBookmarkClick(movie: MovieUiModel)
}

interface MoreLikeThisListener{
    fun onBookmarkClick(movie: MovieUiModel)
    fun onMovieClick(movieId: Int)
}