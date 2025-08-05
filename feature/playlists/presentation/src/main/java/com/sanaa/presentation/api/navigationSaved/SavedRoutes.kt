package com.sanaa.presentation.api.navigationSaved

import com.sanaa.api.AuthenticationApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable

interface SavedDestination {
    fun route(): String
}


@EntryPoint
@InstallIn(SingletonComponent::class)
interface PlayListApiEntryPoint {
    fun authenticationApi(): AuthenticationApi
}

@Serializable
data class SavedDetailsScreenRoute(val listId: Int) : SavedDestination {
    override fun route(): String = "saved/$listId"

    companion object {
        const val PATTERN    = "saved/{listId}"
        const val ARG_LIST_ID = "listId"
    }
}

object PlaylistsScreenRoute : SavedDestination {
    override fun route() = "playlists"

    const val PATTERN = "playlists"
}