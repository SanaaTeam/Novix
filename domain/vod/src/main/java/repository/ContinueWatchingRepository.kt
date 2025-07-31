package repository

import entity.ContinueWatchingItem
import entity.MediaType

interface ContinueWatchingRepository {
    suspend fun getContinueWatchingList(username: String, limit: Int): List<ContinueWatchingItem>
    suspend fun addItem(username: String, mediaId: Int, episodeId: Int?, mediaType: MediaType)
    suspend fun removeItem(mediaId: Int, username: String)
}