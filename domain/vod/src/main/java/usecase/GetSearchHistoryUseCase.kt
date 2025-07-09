package usecase

import usecase.params.SearchHistoryItem
import repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistoryItem> = historyRepo.getSearchHistory()
}
