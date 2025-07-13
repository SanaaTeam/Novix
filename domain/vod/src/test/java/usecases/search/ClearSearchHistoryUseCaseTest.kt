package usecases.search

import exceptions.FailedToDeleteException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SearchHistoryRepository

class ClearSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        clearSearchHistoryUseCase = ClearSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call clearSearchHistory() from SearchHistoryRepository when clear searched history`() =
        runTest {
            // When
            clearSearchHistoryUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.clearSearchHistory()
            }
        }

    @Test
    fun `execute() should throw FailedToDeleteException when try to clear searched history failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.clearSearchHistory()
            } throws FailedToDeleteException("Search History")

            // When, Then
            assertThrows<FailedToDeleteException> {
                clearSearchHistoryUseCase.execute()
            }
        }
}