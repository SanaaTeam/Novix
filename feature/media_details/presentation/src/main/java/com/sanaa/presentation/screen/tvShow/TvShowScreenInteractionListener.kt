package com.sanaa.presentation.screen.tvShow

import com.sanaa.presentation.model.GenreUiModel

interface TvShowScreenInteractionListener {
    fun onBackClicked()
    fun onViewReviewsClicked(tvShowId: Int)
    fun onActorClicked(actorId: Int)
    fun onSeasonNumberClicked(seasonNumber: Int)
    fun onEpisodeClicked(tvShowId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onPlayTrailerClicked()
    fun onRateClicked()
    fun onDismissRateBottomSheet()
    fun onGenreClicked(genre: GenreUiModel)
    fun onRetryLoadDetails()
    fun onLoginButtonClick()
    fun onRatingChanged(newRating: Int)
    fun onDismissLoginBottomSheet()
    fun onSubmitRateBottomSheet()
    fun onSnackBarDismiss()
}