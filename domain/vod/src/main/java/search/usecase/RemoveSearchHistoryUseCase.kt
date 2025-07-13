package search.usecase

import repository.SearchHistoryRepository

class RemoveSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun execute(id: Int) {
        historyRepo.removeSearchHistoryById(id)
    }
}