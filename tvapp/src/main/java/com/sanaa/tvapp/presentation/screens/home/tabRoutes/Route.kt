package com.sanaa.tvapp.presentation.screens.home.tabRoutes

import kotlinx.serialization.Serializable

@Serializable
open class HomeMediaTapRoute

@Serializable
object HomeMoviesTapRoute : HomeMediaTapRoute()

@Serializable
object HomeTvShowsTapRoute : HomeMediaTapRoute()