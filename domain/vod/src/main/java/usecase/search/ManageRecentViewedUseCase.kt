package usecase.search

import kotlinx.coroutines.flow.Flow
import repository.HistoryRepository
import usecase.search.search_param.MediaType

class ManageRecentViewedUseCase(
    private val historyRepo: HistoryRepository,
) {
    suspend fun addRecentViewed(item: RecentViewedMedia) = historyRepo.addRecentViewedMedia(item)
    suspend fun clearRecentViewed() = historyRepo.clearRecentViewed()
    suspend fun getRecentViewed(): Flow<List<RecentViewedMedia>> = historyRepo.getRecentViewed(RECENT_MEDIA_SIZE_LIMIT)

    data class RecentViewedMedia(
        val id: Int,
        val posterImageUrl: String,
        val mediaType: MediaType,
        val isSaved: Boolean,
    )
    companion object {
        private const val RECENT_MEDIA_SIZE_LIMIT = 10
    }
}