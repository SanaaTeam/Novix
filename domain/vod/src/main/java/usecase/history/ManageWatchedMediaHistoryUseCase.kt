package usecase.history

import entity.MediaHistoryItem
import repository.HistoryRepository
import usecase.search.search_param.MediaType

class ManageWatchedMediaHistoryUseCase(
    private val repository: HistoryRepository
) {

    suspend fun getMediaHistory(
        mediaType: MediaType?,
        genreId: Int?
    ): List<MediaHistoryItem> =
        repository.getWatchedMediaHistory(mediaType, genreId)

    suspend fun addWatchedMediaHistory(mediaHistoryItem: MediaHistoryItem) {
        repository.addWatchedMediaHistory(mediaHistoryItem)
    }

}