package usecase.continueWatch

import entity.ContinueWatchingItem
import entity.MediaType
import repository.ContinueWatchingRepository

class ManageContinueWatchingUseCase(
    private val repository: ContinueWatchingRepository
) {

    suspend fun getContinueWatchingList(): List<ContinueWatchingItem> =
        repository.getContinueWatchingList(CONTINUE_WATCHING_LIMIT)

    suspend fun addItem(
        mediaId: Int,
        episodeId: Int? = null,
        mediaType: MediaType
    ) {
        repository.addItem(
            mediaId = mediaId,
            episodeId = episodeId,
            mediaType = mediaType
        )
    }

    suspend fun removeItem(mediaId: Int) {
        repository.removeItem(mediaId)
    }

    companion object {
        private const val CONTINUE_WATCHING_LIMIT = 15
    }
}