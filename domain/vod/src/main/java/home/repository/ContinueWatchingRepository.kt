package home.repository

import entity.MediaItem
import kotlinx.coroutines.flow.Flow

interface ContinueWatchingRepository {
    fun getContinueWatchingContent(): Flow<List<MediaItem>>
}