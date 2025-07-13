package search.usecase

import repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute() = historyRepo.clearSearchHistory()
}