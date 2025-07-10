package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
}