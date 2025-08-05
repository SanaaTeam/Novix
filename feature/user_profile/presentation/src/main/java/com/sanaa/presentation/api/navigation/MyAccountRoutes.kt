package com.sanaa.presentation.api.navigation

import kotlinx.serialization.Serializable

open class MyAccountRoutes
@Serializable
object MyAccountScreenRoute :MyAccountRoutes()
@Serializable
object MyRatingScreenRoute : MyAccountRoutes()

@Serializable
object WatchingHistoryScreenRoute : MyAccountRoutes()

@Serializable
object ChangePasswordScreen: MyAccountRoutes()



