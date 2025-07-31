package usecase.continueWatch

import entity.ContinueWatchingItem
import entity.MediaType
import repository.ContinueWatchingRepository

class ManageContinueWatchingUseCase(
    private val repository: ContinueWatchingRepository
) {

    suspend fun getContinueWatchingList(username: String): List<ContinueWatchingItem> {
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