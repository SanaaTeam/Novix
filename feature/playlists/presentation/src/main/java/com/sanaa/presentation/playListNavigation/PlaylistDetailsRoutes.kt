package com.sanaa.presentation.playListNavigation

import com.sanaa.api.AuthenticationApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable

interface PlaylistDestination {
    fun route(): String
}


@EntryPoint
@InstallIn(SingletonComponent::class)
interface PlayListApiEntryPoint {
    fun authenticationApi(): AuthenticationApi
}

@Serializable
data class SavedDetailsScreenRoute(val listId: Int, val title: String) : PlaylistDestination {
    override fun route(): String = "saved/$listId/$title"

    companion object {
        const val PATTERN = "saved/{listId}/{title}"
        const val ARG_LIST_ID = "listId"
        const val ARG_LIST_TITLE = "title"
    }
}

object PlaylistsScreenRoute : PlaylistDestination {
    override fun route() = "playlists"

    const val PATTERN = "playlists"
}