package usecase.history

import entity.MediaHistoryItem
import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.search.search_param.MediaType
import javax.inject.Inject

class ManageWatchingHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend fun getWatchingHistory(
        username: String,
        mediaType: MediaType? = null
    ): Flow<List<MediaHistoryItem>> =
        repository.getWatchingHistory(username = username, mediaType = mediaType)

    suspend fun updateLastWatchedTime(
        username: String,
        mediaId: Int,
        mediaType: MediaType
    ) {
        repository.updateLastWatchedTime(username = username, mediaId = mediaId, mediaType = mediaType)
    }

    suspend fun addWatchedMediaHistory(
        mediaHistoryItem: MediaHistoryItem,
        username: String
    ) {
        repository.addWatchedMediaHistory(username = username, media = mediaHistoryItem)
    }
} 