package search.usecase

import search.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute() = historyRepo.clearSearchHistory()
}