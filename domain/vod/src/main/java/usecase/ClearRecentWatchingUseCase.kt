package usecase

import repository.SearchHistoryRepository

class ClearRecentWatchingUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute() = historyRepo.clearRecentWatching()
}