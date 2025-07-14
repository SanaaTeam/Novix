package usecases.search

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import search.repository.SearchHistoryRepository
import search.usecase.GetRecentViewedUseCase
import search.usecase.search_param.MediaType
import search.usecase.search_param.RecentViewedMedia

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
            coEvery {
                searchHistoryRepository.getRecentViewed(any())
            } returns flowOf(recentViewedList)

            // When
            val result = getRecentViewedUseCase.execute().single()

            // Then
            assertThat(result).isEqualTo(recentViewedList)
        }

    @Test
    fun `execute() should return empty list when no recent viewed items are available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getRecentViewed(any()) } returns flowOf(emptyList())

            // When
            val result = getRecentViewedUseCase.execute().single()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `execute() should throw RetrievingDataFailureException when when try to get recent viewed media failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.getRecentViewed(any())
            } throws RetrievingDataFailureException("Recent View")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                getRecentViewedUseCase.execute()
            }
        }

    companion object {
        private val recentViewedList = listOf(
            RecentViewedMedia(1, "https://image.com/1", MediaType.MOVIE, false),
            RecentViewedMedia(2, "https://image.com/2", MediaType.MOVIE, false)
        )
    }
}