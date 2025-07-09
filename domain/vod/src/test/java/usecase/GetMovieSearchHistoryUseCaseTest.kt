package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class GetMovieSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getSearchHistoryUseCase: GetMovieSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        getSearchHistoryUseCase = GetMovieSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call getMovieSearchHistory() from SearchHistoryRepository when get searched movie history`() =
        runTest {
            // When
            getSearchHistoryUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.getMovieSearchHistory()
            }
        }
}
