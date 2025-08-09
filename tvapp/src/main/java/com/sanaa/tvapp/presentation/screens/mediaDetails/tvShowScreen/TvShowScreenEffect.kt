package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.GenreUiModel


sealed interface TvShowDetailsScreenEffects {
    data class NavigateToActorScreen(val actorId: Int) : TvShowDetailsScreenEffects
    data class NavigateToReviewsScreen(val seriesId: Int) : TvShowDetailsScreenEffects
    data class NavigateToEpisodeDetailsScreen(
        val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
    ) : TvShowDetailsScreenEffects

    object NavigateBack : TvShowDetailsScreenEffects
    data class PlayTrailer(val trailerUrl: String?) : TvShowDetailsScreenEffects
    data class NavigateToMovieCategoriesScreen(val category: GenreUiModel) : TvShowDetailsScreenEffects
    data object  ShowErrorSnackBar : TvShowDetailsScreenEffects
    data object ShowSuccessSnackBar : TvShowDetailsScreenEffects
    object NavigateToLogin : TvShowDetailsScreenEffects
}