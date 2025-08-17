package com.sanaa.presentation.screen.tvShow

import com.sanaa.presentation.model.GenreUiModel

sealed class TvShowScreenEffects {
    data class NavigateToActorScreen(val actorId: Int) : TvShowScreenEffects()
    data class NavigateToReviewsScreen(val tvShowId: Int) : TvShowScreenEffects()
    data class NavigateToEpisodeDetailsScreen(
        val tvShowId: Int, val seasonNumber: Int, val episodeNumber: Int
    ) : TvShowScreenEffects()

    object NavigateBack : TvShowScreenEffects()
    data class PlayTrailer(val trailerUrl: String?) : TvShowScreenEffects()
    data class NavigateToMovieCategoriesScreen(val category: GenreUiModel) : TvShowScreenEffects()
    data class ShowErrorSnackBar(val message: String) : TvShowScreenEffects()
    object NavigateToLogin : TvShowScreenEffects()
}