package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchHistoryOutputItem

class GetSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(): List<SearchHistoryOutputItem> = historyRepo.getSearchHistory()
}
