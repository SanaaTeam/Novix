package usecase

import com.google.common.truth.Truth.assertThat
import extensions.now
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import usecase.search.SearchHistory

class GetSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        getSearchHistoryUseCase = GetSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should return history list when available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory() } returns SearchHistoryList

            // When
            val result = getSearchHistoryUseCase.execute()

            // Then
            assertThat(result).isEqualTo(SearchHistoryList)
        }

    @Test
    fun `execute() should return empty list when there are no search history available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory() } returns emptyList()

            // When
            val result = getSearchHistoryUseCase.execute()

            // Then
            assertThat(result).isEmpty()
        }

    companion object {
        private val SearchHistoryList = listOf(
            SearchHistory("Search Query 1", timestamp = LocalDateTime.now()),
            SearchHistory("Search Query 2", timestamp = LocalDateTime.now()),
        )
    }
}
