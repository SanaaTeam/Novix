package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.GenreUiModel

interface TvShowScreenInteractionListener {
    fun onBackClicked()
    fun onActorClicked(actorId: Int)
    fun onSeasonNumberClicked(seasonNumber: Int)
    fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onPlayTrailerClicked()
    fun onRateClicked()
    fun onDismissRateBottomSheet()
    fun onDismissAnyBottomSheet()
    fun onSaveSeriesClicked()
    fun onGenreClicked(genre: GenreUiModel)
    fun onRetryLoadDetails()
    fun onLoginButtonClick()
    fun onRatingChanged(newRating: Int)
    fun onDismissLoginBottomSheet()
    fun onSubmitRateBottomSheet()
}