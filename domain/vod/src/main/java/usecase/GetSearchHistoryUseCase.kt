package usecase

import usecase.search.SearchHistoryItem
import repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistoryItem> = historyRepo.getSearchHistory()
}
