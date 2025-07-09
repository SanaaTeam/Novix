package usecase

import repository.SearchHistoryRepository

class RemoveSearchHistoryItemUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(id: String) {
        historyRepo.removeSearchHistoryItem(id)
    }
}