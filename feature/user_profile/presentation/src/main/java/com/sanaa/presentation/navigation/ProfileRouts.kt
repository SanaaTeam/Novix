package com.sanaa.presentation.navigation

import kotlinx.serialization.Serializable

open class ProfileRouts
@Serializable
object MyAccountScreenRoute :ProfileRouts()
@Serializable
object MyRatingScreenRoute : ProfileRouts()

@Serializable
object WatchingHistoryScreenRoute : ProfileRouts()

@Serializable
object ChangePasswordScreenRoute: ProfileRouts()



