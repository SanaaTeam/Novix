package com.sanaa.tvapp.presentation.screens.navigation

import kotlinx.serialization.Serializable

open class ScreenRoute

@Serializable
class ScreensRoute {
    @Serializable
    object LoginRoute : ScreenRoute()

    @Serializable
    data class MovieDetailsRoute(val movieId: Int) : ScreenRoute()

    @Serializable
    data class TvShowDetailsRoute(val seriesId: Int) : ScreenRoute()

    @Serializable
    data class ActorDetailsRoute(val actorId: Int) : ScreenRoute()

    @Serializable
    data class EpisodeDetailsRoute(
        val seriesId: Int,
        val seasonNumber: Int,
        val episodeNumber: Int,
    ) : ScreenRoute()

    @Serializable
    object ChangePasswordScreenRoute : ScreenRoute()

    @Serializable
    object MyRatingScreenRoute : ScreenRoute()

    @Serializable
    object WatchingHistoryScreenRoute : ScreenRoute()

    @Serializable
    data class GenreMovieScreenRoute(
        val genreId: Int,
        val genreName: String,
    ) : ScreenRoute()

}

