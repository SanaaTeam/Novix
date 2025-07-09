package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchHistoryInputItem

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistoryInputItem> = historyRepo.getSearchHistory()
}
