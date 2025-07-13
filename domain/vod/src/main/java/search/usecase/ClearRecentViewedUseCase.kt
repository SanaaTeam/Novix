package search.usecase

import repository.SearchHistoryRepository

class ClearRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute() = historyRepo.clearRecentViewed()
}