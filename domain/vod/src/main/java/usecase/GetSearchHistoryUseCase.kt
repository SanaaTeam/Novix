package usecase

import entity.SearchHistoryItem
import repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistoryItem> = historyRepo.getSearchHistory()
}
