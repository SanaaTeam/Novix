package usecase

import repository.SearchHistoryRepository

class AddRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(id: String) = historyRepo.addRecentViewedItem(id)
}