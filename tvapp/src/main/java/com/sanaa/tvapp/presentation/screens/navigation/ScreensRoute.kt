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
    data class TvShowDetails(val tvShowId: Int) : ScreenRoute()

    @Serializable
    data class ActorDetails(val actorId: Int) : ScreenRoute()



}

