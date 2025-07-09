package usecase

import repository.SearchHistoryRepository
import usecase.search.SearchHistoryItem

class RemoveSearchHistoryItemUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(item: SearchHistoryItem) {
        historyRepo.removeSearchHistoryItem(item)
    }
}