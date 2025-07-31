package usecase.continueWatch

import entity.ContinuableMedia
import entity.ContinueWatchingItem
import entity.MediaType
import kotlinx.coroutines.flow.Flow
import repository.ContinueWatchingRepository

class ManageContinueWatchingUseCase(
    private val repository: ContinueWatchingRepository
) {
    fun getContinueWatchingList(username: String): Flow<List<ContinueWatchingItem>> {
        return repository.getContinueWatchingList(username, CONTINUE_WATCHING_LIMIT)
    }

    suspend fun addItem(username: String, continuableMedia: ContinuableMedia, episodeId: Int? = null) {
        val itemToSave = ContinueWatchingItem(
            media = continuableMedia,
            episodeId = episodeId
        )
        repository.addItem(username, itemToSave)
    }

    companion object {
        private const val CONTINUE_WATCHING_LIMIT = 15
    }
}