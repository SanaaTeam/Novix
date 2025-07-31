package usecase.continueWatch

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

    suspend fun addItem(
        username: String,
        mediaId: Int,
        episodeId: Int? = null,
        mediaType: MediaType
    ) {
        repository.addItem(
            username = username,
            mediaId = mediaId,
            episodeId = episodeId,
            mediaType = mediaType
        )
    }

    suspend fun removeItem(username: String, mediaId: Int) {
        repository.removeItem(mediaId, username)
    }

    companion object {
        private const val CONTINUE_WATCHING_LIMIT = 15
    }
}