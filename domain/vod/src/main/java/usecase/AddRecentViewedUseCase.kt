package usecase

import repository.SearchHistoryRepository
import usecase.search.RecentViewedMedia

class AddRecentViewedUseCase(
    private val historyRepo: SearchHistoryRepository
) {
    suspend fun execute(item: RecentViewedMedia) = historyRepo.addRecentViewedMedia(item)
}