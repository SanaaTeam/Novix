package com.sanaa.presentation.navigation

import kotlinx.serialization.Serializable

open class ProfileDestinations

@Serializable
object AccountMainScreenRoute: ProfileDestinations()

@Serializable
object WatchingHistoryScreenRoute: ProfileDestinations()

@Serializable
object MyRatingScreenRoute: ProfileDestinations()


@Serializable
object ChangPasswordRoute: ProfileDestinations()
