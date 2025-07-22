package home.usecase

import entity.MediaItem
import home.repository.ContinueWatchingRepository
import kotlinx.coroutines.flow.Flow

class GetContinueWatchingContentUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> {
        return continueWatchingRepository.getContinueWatchingContent()
    }
}