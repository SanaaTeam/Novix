package usecase

import exceptions.FailedToAddException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SearchHistoryRepository
import usecase.search.MediaType
import usecase.search.RecentViewedMedia

class AddRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var addRecentViewedUseCase: AddRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        addRecentViewedUseCase = AddRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call addRecentViewedItem() from SearchHistoryRepository with the correct input when add recent viewed movies`(): Unit =
        runTest {
            // Given
            val item = RecentViewedMedia(1, "https://image.com", MediaType.MOVIE)

            // When
            addRecentViewedUseCase.execute(item)

            // Then
            coVerify {
                searchHistoryRepository.addRecentViewedMedia(item)
            }
        }

    @Test
    fun `execute() should throw FailedToAddException when there is a problem with adding the recent viewed item`(): Unit =
        runTest {
            // Given
            val item = RecentViewedMedia(1, "https://image.com", MediaType.MOVIE)
            coEvery {
                searchHistoryRepository.addRecentViewedMedia(item)
            } throws FailedToAddException("Recent View Item")

            // When, Then
            assertThrows<FailedToAddException> {
                addRecentViewedUseCase.execute(item)
            }
        }
}