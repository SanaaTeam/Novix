package usecase.search

import com.google.common.truth.Truth.assertThat
import exceptions.FailedToAddException
import exceptions.FailedToDeleteException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.HistoryRepository
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.search_param.MediaType

class ManageRecentViewedUseCaseTest {
    private var searchHistoryRepository: HistoryRepository = mockk(relaxed = true)
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        manageRecentViewedUseCase = ManageRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `addRecentViewed() should call addRecentViewedMedia() from SearchHistoryRepository with the correct input when add recent viewed movies`(): Unit =
        runTest {
            // Given
            val item = RecentViewedMedia(1, "https://image.com", MediaType.MOVIE, false)

            // When
            manageRecentViewedUseCase.addRecentViewed(item)

            // Then
            coVerify {
                searchHistoryRepository.addRecentViewedMedia(item)
            }
        }

    @Test
    fun `addRecentViewed() should throw FailedToAddException when there is a problem with adding the recent viewed item`(): Unit =
        runTest {
            // Given
            val item = RecentViewedMedia(1, "https://image.com", MediaType.MOVIE, false)
            coEvery {
                searchHistoryRepository.addRecentViewedMedia(item)
            } throws FailedToAddException("Recent View Item")

            // When, Then
            assertThrows<FailedToAddException> {
                manageRecentViewedUseCase.addRecentViewed(item)
            }
        }


    @Test
    fun `getRecentViewed() should return recent viewed list when available`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.getRecentViewed(any())
            } returns flowOf(recentViewedList)

            // When
            val result = manageRecentViewedUseCase.getRecentViewed().single()

            // Then
            assertThat(result).isEqualTo(recentViewedList)
        }

    @Test
    fun `getRecentViewed() should return empty list when no recent viewed items are available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getRecentViewed(any()) } returns flowOf(emptyList())

            // When
            val result = manageRecentViewedUseCase.getRecentViewed().single()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getRecentViewed() should throw RetrievingDataFailureException when when try to get recent viewed media failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.getRecentViewed(any())
            } throws RetrievingDataFailureException("Recent View")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                manageRecentViewedUseCase.getRecentViewed()
            }
        }

    @Test
    fun `clearRecentViewed() should call clearRecentViewed() from SearchHistoryRepository when clear recent viewed movies`() =
        runTest {
            // When
            manageRecentViewedUseCase.clearRecentViewed()

            // Then
            coVerify {
                searchHistoryRepository.clearRecentViewed()

            }
        }

    @Test
    fun `clearRecentViewed() should throw FailedToDeleteException when there is a problem with adding the recent viewed item`(): Unit =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.clearRecentViewed()
            } throws FailedToDeleteException("Recent View Item")

            // When, Then
            assertThrows<FailedToDeleteException> {
                manageRecentViewedUseCase.clearRecentViewed()
            }
        }

    companion object {
        private val recentViewedList = listOf(
            RecentViewedMedia(1, "https://image.com/1", MediaType.MOVIE, false),
            RecentViewedMedia(2, "https://image.com/2", MediaType.MOVIE, false)
        )
    }
}