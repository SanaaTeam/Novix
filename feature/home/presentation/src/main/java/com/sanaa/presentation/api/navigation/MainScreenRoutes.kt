package com.sanaa.tudee_assistant.presentation.navigation

import kotlinx.serialization.Serializable

open class MainScreenRoutes

@Serializable
object HomeScreenRoute : MainScreenRoutes()

@Serializable
object SearchScreenRoute : MainScreenRoutes()

@Serializable
object PlayListScreenRoute : MainScreenRoutes()

@Serializable
object SavedContentScreenRoute : MainScreenRoutes()

@Serializable
object UserProfileScreenRoute : MainScreenRoutes()
