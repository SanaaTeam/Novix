package com.sanaa.presentation.api.navigationSaved

import com.sanaa.api.AuthenticationApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable

open class SavedRoutes

@Serializable
object PlaylistScreenRoute : SavedRoutes()

@Serializable
object SavedDetailsScreenRoute : SavedRoutes()


@EntryPoint
@InstallIn(SingletonComponent::class)
interface PlayListApiEntryPoint {
    fun authenticationApi(): AuthenticationApi
}