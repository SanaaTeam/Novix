package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchHistory
import usecase.search.SearchHistoryInputItem

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistory> = historyRepo.getSearchHistory()
}
