package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class GetSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        getSearchHistoryUseCase = GetSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call getSearchHistory() from SearchHistoryRepository when get searched history`() =
        runTest {
            // When
            getSearchHistoryUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.getSearchHistory()
            }
        }
}
