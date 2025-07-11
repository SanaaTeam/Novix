package usecase

import kotlinx.coroutines.flow.Flow
import repository.SearchHistoryRepository
import usecase.search.RecentViewedItem

class GetRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): Flow<List<RecentViewedItem>> = historyRepo.getRecentViewed()
}