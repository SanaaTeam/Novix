package usecase.history

import entity.Genre
import entity.MediaType
import repository.HistoryRepository

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

    data class MediaHistoryItem(
        val id: Int,
        val posterImageUrl: String,
        val mediaType: MediaType,
        val genres: List<Genre>
    )
}