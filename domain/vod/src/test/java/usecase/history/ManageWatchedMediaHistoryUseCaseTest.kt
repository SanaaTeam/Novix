package usecase.history

import entity.MediaHistoryItem
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import repository.HistoryRepository
import usecase.search.search_param.MediaType
import kotlin.test.Test

class ManageWatchedMediaHistoryUseCaseTest {
    private val historyRepository: HistoryRepository = mockk(relaxed = true)
    private lateinit var manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase

    @BeforeEach
    fun setUp() {
        manageWatchedMediaHistoryUseCase = ManageWatchedMediaHistoryUseCase(historyRepository)
    }

    @Test
    fun `getMediaHistory should call HistoryRepository when try to get watched media history`() =
        runTest {
            // Given
            val username = "User"
            val mediaType = MediaType.MOVIE
            val genreId = 1
            manageWatchedMediaHistoryUseCase.getMediaHistory(
                username = username,
                mediaType = mediaType,
                genreId = genreId
            )

            coVerify {
                historyRepository.getWatchedMediaHistory(
                    username = username,
                    mediaType = mediaType,
                    genreId = genreId
                )
            }
        }

    @Test
    fun `addWatchedMediaHistory should call HistoryRepository when try to add watched media history`() =
        runTest {
            // Given
            val username = "User"
            val mediaHistoryItem = MediaHistoryItem(
                1, "http://image.com", MediaType.MOVIE, emptyList()
            )
            val mediaType = MediaType.MOVIE
            val genreId = 1
            manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
                mediaHistoryItem = mediaHistoryItem,
                username = username
            )

            coVerify {
                historyRepository.addWatchedMediaHistory(
                    username = username,
                    media = mediaHistoryItem
                )
            }
        }

}