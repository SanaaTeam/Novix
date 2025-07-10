package usecase

import repository.SearchHistoryRepository
import usecase.search.RecentViewedItem

class GetRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<RecentViewedItem> = historyRepo.getRecentViewed()
}