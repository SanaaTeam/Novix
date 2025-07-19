package com.sanaa.presentation.screen.series

sealed class SeriesScreenEffects {
    data class NavigateToActorScreen(val actorId: Int) : SeriesScreenEffects()
    data class NavigateToReviewsScreen(val seriesId: Int) : SeriesScreenEffects()
    data class NavigateToEpisodeDetailsScreen(
        val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
    ) : SeriesScreenEffects()

    object NavigateBack : SeriesScreenEffects()
    data class PlayTrailer(val trailerUrl: String?) : SeriesScreenEffects()
    data class NavigateToMovieCategoriesScreen(val categoryId: String) : SeriesScreenEffects()

}