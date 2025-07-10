package usecase

import com.google.common.truth.Truth.assertThat
import extensions.now
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
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
            coEvery { searchHistoryRepository.getSearchHistory() } returns flowOf(SearchHistoryList)

            // When
            val result = getSearchHistoryUseCase.execute().single()

            // Then
            assertThat(result).isEqualTo(SearchHistoryList)
        }

    @Test
    fun `execute() should return empty list when there are no search history available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory() } returns flowOf(emptyList())

            // When
            val result = getSearchHistoryUseCase.execute().single()

            // Then
            assertThat(result).isEmpty()
        }

    companion object {
        private val SearchHistoryList = listOf(
            SearchHistory(1, "Search Query 1", timestamp = LocalDateTime.now()),
            SearchHistory(2, "Search Query 2", timestamp = LocalDateTime.now()),
        )
    }
}
