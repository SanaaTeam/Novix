package com.sanaa.presentation.app.navigation

import kotlinx.serialization.Serializable


open class AppRoute



@Serializable
object HomeScreenRoute : AppRoute()

@Serializable
object SearchScreenRoute : AppRoute()

@Serializable
object PlayListScreenRoute : AppRoute()

@Serializable
object CategoryScreenRoute : AppRoute()

@Serializable
object UserProfileScreenRoute : AppRoute()

@Serializable
object TrendingMoviesScreenRoute : AppRoute()

@Serializable
object TrendingTvShowsScreenRoute : AppRoute()

@Serializable
object TrendingPeopleScreenRoute : AppRoute()

@Serializable
object TopRatedMediaScreenRoute : AppRoute()

@Serializable
object WatchingMediaHistoryScreenRoute : AppRoute()
