package usecase.history

import entity.MediaHistoryItem
import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.search.search_param.MediaType
import javax.inject.Inject

class ManageWatchedMediaHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
) {

    suspend fun getMediaHistory(
        username: String,
        mediaType: MediaType?,
        genreId: Int?,
    ): Flow<List<MediaHistoryItem>> =
        repository.getWatchedMediaHistory(
            username = username,
            mediaType = mediaType,
            genreId = genreId
        )

    suspend fun addWatchedMediaHistory(mediaHistoryItem: MediaHistoryItem, username: String) =
        repository.addWatchedMediaHistory(
            username = username,
            media = mediaHistoryItem
        )
}