package com.sanaa.presentation.screen.series

import com.sanaa.presentation.model.GenreUiModel

sealed class SeriesScreenEffects {
    data class NavigateToActorScreen(val actorId: Int) : SeriesScreenEffects()
    data class NavigateToReviewsScreen(val seriesId: Int) : SeriesScreenEffects()
    data class NavigateToEpisodeDetailsScreen(
        val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
    ) : SeriesScreenEffects()

    object NavigateBack : SeriesScreenEffects()
    data class PlayTrailer(val trailerUrl: String?) : SeriesScreenEffects()
    data class NavigateToMovieCategoriesScreen(val category: GenreUiModel) : SeriesScreenEffects()
    data object  ShowErrorSnackBar : SeriesScreenEffects()
    data object ShowSuccessSnackBar : SeriesScreenEffects()
    object NavigateToLogin : SeriesScreenEffects()
}