package usecase

import exceptions.FailedToDeleteException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import search.repository.SearchHistoryRepository
import search.usecase.RemoveSearchHistoryUseCase

class RemoveSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var removeSearchHistoryUseCase: RemoveSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        removeSearchHistoryUseCase = RemoveSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call removeSearchHistoryById() from SearchHistoryRepository when remove item form the search history`() =
        runTest {
            // Given
            val id = 1

            // When
            removeSearchHistoryUseCase.execute(id)

            // Then
            coVerify {
                searchHistoryRepository.removeSearchHistoryById(id)
            }
        }

    @Test
    fun `execute() should throw FailedToDeleteException when try to clear recent viewed failed`() =
        runTest {
            // Given
            val id = 1
            coEvery {
                searchHistoryRepository.removeSearchHistoryById(id)
            } throws FailedToDeleteException("Search History")

            // When, Then
            assertThrows<FailedToDeleteException> {
                removeSearchHistoryUseCase.execute(id)
            }
        }
}