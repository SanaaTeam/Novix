package search.usecase

import search.repository.SearchHistoryRepository

class RemoveSearchHistoryUseCase(
    private val historyRepo: SearchHistoryRepository,
) {
    suspend fun execute(id: Int) {
        historyRepo.removeSearchHistoryById(id)
    }
}