package usecases.search

import exceptions.FailedToDeleteException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import search.repository.SearchHistoryRepository
import search.usecase.ClearRecentViewedUseCase

class ClearRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var clearRecentViewedUseCase: ClearRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        clearRecentViewedUseCase = ClearRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call clearRecentViewed() from SearchHistoryRepository when clear recent viewed movies`() =
        runTest {
            // When
            clearRecentViewedUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.clearRecentViewed()

            }
        }


    @Test
    fun `execute() should throw FailedToDeleteException when there is a problem with adding the recent viewed item`(): Unit =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.clearRecentViewed()
            } throws FailedToDeleteException("Recent View Item")

            // When, Then
            assertThrows<FailedToDeleteException> {
                clearRecentViewedUseCase.execute()
            }
        }
}