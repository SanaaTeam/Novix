package usecases.search

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import extensions.now
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SearchHistoryRepository
import search.search.SearchHistory

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
            coEvery { searchHistoryRepository.getSearchHistory(any()) } returns flowOf(
                SearchHistoryList
            )

            // When
            val result = getSearchHistoryUseCase.execute().single()

            // Then
            assertThat(result).isEqualTo(SearchHistoryList)
        }

    @Test
    fun `execute() should return empty list when there are no search history available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory(any()) } returns flowOf(emptyList())

            // When
            val result = getSearchHistoryUseCase.execute().single()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `execute() should throw NotFoundException when when try to search in the history failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.getSearchHistory(any())
            } throws RetrievingDataFailureException("Search History")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                getSearchHistoryUseCase.execute()
            }
        }

    companion object {
        private val SearchHistoryList = listOf(
            SearchHistory(1, "Search Query 1", timestamp = LocalDateTime.now()),
            SearchHistory(2, "Search Query 2", timestamp = LocalDateTime.now()),
        )
    }
}
