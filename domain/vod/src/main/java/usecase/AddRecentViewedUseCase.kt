package usecase

import repository.SearchHistoryRepository
import usecase.search.RecentViewedItem

class AddRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(item: RecentViewedItem) = historyRepo.addRecentViewedItem(item)
}