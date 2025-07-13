package search.usecase

import kotlinx.coroutines.flow.Flow
import repository.SearchHistoryRepository
import usecase.search.RecentViewedMedia

class GetRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun execute(): Flow<List<RecentViewedMedia>> =
        historyRepo.getRecentViewed(RECENT_MEDIA_SIZE_LIMIT)

    companion object {
        private const val RECENT_MEDIA_SIZE_LIMIT = 10
    }
}