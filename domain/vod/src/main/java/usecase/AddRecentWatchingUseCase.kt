package usecase

import repository.SearchHistoryRepository
import usecase.search.RecentWatchingItem

class AddRecentWatchingUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(item: RecentWatchingItem) = historyRepo.addRecentWatching(item)
}