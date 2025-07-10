package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchHistory

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistory> = historyRepo.getSearchHistory()
}
