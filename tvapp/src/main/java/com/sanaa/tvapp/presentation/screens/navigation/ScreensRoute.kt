package com.sanaa.tvapp.presentation.screens.navigation

import kotlinx.serialization.Serializable

open class ScreenRoute

@Serializable
class ScreensRoute {

    @Serializable
    object Login : ScreenRoute()

    @Serializable
    data class MovieDetails(val movieId: Int) : ScreenRoute()

    @Serializable
    data class TvShowDetails(val seriesId: Int) : ScreenRoute()

    @Serializable
    data class ActorDetails(val actorId: Int) : ScreenRoute()

    @Serializable
    data class EpisodeDetails(val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int) :
        ScreenRoute()


    @Serializable
    object ChangePasswordScreenRoute : ScreenRoute()

    @Serializable
    object MyRatingScreenRoute : ScreenRoute()

    @Serializable
    object WatchingHistoryScreenRoute : ScreenRoute()
}

