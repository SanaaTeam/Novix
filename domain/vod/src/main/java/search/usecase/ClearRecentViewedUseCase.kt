package search.usecase

import search.repository.SearchHistoryRepository

class ClearRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute() = historyRepo.clearRecentViewed()
}