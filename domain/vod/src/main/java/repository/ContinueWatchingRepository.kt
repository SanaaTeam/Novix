package repository

import entity.ContinueWatchingItem
import entity.MediaType
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingItem>>
    suspend fun addItem(username: String, mediaId: Int, episodeId: Int?, mediaType: MediaType)
}