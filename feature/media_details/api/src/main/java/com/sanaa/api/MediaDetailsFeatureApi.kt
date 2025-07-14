package com.sanaa.api

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object MediaDetailsFeatureApi {
    private const val ROUTE_PATTERN = "media_details"
    const val ARG_MEDIA_ID = "mediaId"

    val route: String = "$ROUTE_PATTERN/{$ARG_MEDIA_ID}"

    val arguments: List<NamedNavArgument> = listOf(
        navArgument(ARG_MEDIA_ID) { type = NavType.StringType }
    )

    fun buildRoute(mediaId: String): String = "$ROUTE_PATTERN/$mediaId"
}