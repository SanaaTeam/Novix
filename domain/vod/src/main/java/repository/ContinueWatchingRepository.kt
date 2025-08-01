package repository

import entity.ContinueWatchingItem
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingItem>>
    suspend fun addMedia(username: String, item: ContinueWatchingItem)
}