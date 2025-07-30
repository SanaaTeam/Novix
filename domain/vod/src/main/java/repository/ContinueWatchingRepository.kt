package repository

import entity.ContinueWatchingItem
import entity.MediaType

interface ContinueWatchingRepository {

    suspend fun getContinueWatchingList(limit: Int): List<ContinueWatchingItem>

    suspend fun addItem(
        mediaId: Int,
        episodeId: Int?,
        mediaType: MediaType
    )

    suspend fun removeItem(mediaId: Int)
}