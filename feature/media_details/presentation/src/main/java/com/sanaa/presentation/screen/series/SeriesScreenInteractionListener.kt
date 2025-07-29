package com.sanaa.presentation.screen.series

import com.sanaa.presentation.model.GenreUiModel

interface SeriesScreenInteractionListener {
    fun onBackClicked()
    fun onViewReviewsClicked(seriesId: Int)
    fun onActorClicked(actorId: Int)
    fun onSeasonNumberClicked(seasonNumber: Int)
    fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onPlayTrailerClicked()
    fun onRateClicked()
    fun onDismissRateBottomSheet()
    fun onSaveSeriesClicked()
    fun onGenreClicked(genre: GenreUiModel)
    fun onRetryLoadDetails()
    fun onLoginButtonClick()
}