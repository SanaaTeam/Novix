package com.sanaa.presentation.api.navigation

import kotlinx.serialization.Serializable

open class AppRoute

@Serializable
object MainScreenRoute : AppRoute()

@Serializable
object TrendingMoviesScreenRoute : AppRoute()

@Serializable
object TrendingTvShowsScreenRoute : AppRoute()

@Serializable
object TrendingPeopleScreenRoute : AppRoute()

@Serializable
object TopRatedMediaScreenRoute : AppRoute()

@Serializable
object ActorsScreenRoute : AppRoute()
