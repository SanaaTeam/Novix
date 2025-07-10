package usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import usecase.search.RecentViewedItem

class GetRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getRecentViewedUseCase: GetRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        getRecentViewedUseCase = GetRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should return recent viewed list when available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getRecentViewed() } returns flowOf(recentViewedList)

            // When
            val result = getRecentViewedUseCase.execute().single()

            // Then
            assertThat(result).isEqualTo(recentViewedList)
        }

    @Test
    fun `execute() should return empty list when no recent viewed items are available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getRecentViewed() } returns flowOf(emptyList())

            // When
            val result = getRecentViewedUseCase.execute().single()

            // Then
            assertThat(result).isEmpty()
        }

    companion object {
        private val recentViewedList = listOf(
            RecentViewedItem(1, "https://image.com/1"),
            RecentViewedItem(2, "https://image.com/2")
        )
    }
}