package usecase

import repository.SearchHistoryRepository
import usecase.search.RecentWatchingItem


class GetRecentWatchingUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<RecentWatchingItem> = historyRepo.getRecentWatching()
}