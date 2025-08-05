package usecase.history

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.HistoryRepository
import usecase.search.search_param.MediaType
import entity.MediaHistoryItem
import entity.Genre

class ManageWatchingHistoryUseCaseTest {
    private var historyRepository: HistoryRepository = mockk(relaxed = true)
    private lateinit var manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase

    @BeforeEach
    fun setUp() {
        manageWatchingHistoryUseCase = ManageWatchingHistoryUseCase(historyRepository)
    }

    @Test
    fun `getWatchingHistory() should return watching history list when available`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.MOVIE
        coEvery { historyRepository.getWatchingHistory(username, mediaType) } returns flowOf(givenWatchingHistoryList)

        // When
        val result = manageWatchingHistoryUseCase.getWatchingHistory(username, mediaType).first()

        // Then
        assertThat(result).isEqualTo(givenWatchingHistoryList)
    }

    @Test
    fun `getWatchingHistory() should return empty list when there are no watching history available`() = runTest {
        // Given
        val username = "testuser"
        coEvery { historyRepository.getWatchingHistory(username, null) } returns flowOf(emptyList())

        // When
        val result = manageWatchingHistoryUseCase.getWatchingHistory(username, null).first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWatchingHistory() should throw RetrievingDataFailureException when try to retrieve watching history failed`() = runTest {
        // Given
        val username = "testuser"
        coEvery {
            historyRepository.getWatchingHistory(username, null)
        } throws RetrievingDataFailureException("Watching History")

        // When, Then
        assertThrows<RetrievingDataFailureException> {
            manageWatchingHistoryUseCase.getWatchingHistory(username, null).first()
        }
    }

    @Test
    fun `updateLastWatchedTime() should call updateLastWatchedTime from HistoryRepository`() = runTest {
        // Given
        val username = "testuser"
        val mediaId = 1
        val mediaType = MediaType.MOVIE

        // When
        manageWatchingHistoryUseCase.updateLastWatchedTime(username, mediaId, mediaType)

        // Then
        coVerify {
            historyRepository.updateLastWatchedTime(username, mediaId, mediaType)
        }
    }

    @Test
    fun `updateLastWatchedTime() should throw RetrievingDataFailureException when try to update last watched time failed`() = runTest {
        // Given
        val username = "testuser"
        val mediaId = 1
        val mediaType = MediaType.MOVIE
        coEvery {
            historyRepository.updateLastWatchedTime(username, mediaId, mediaType)
        } throws RetrievingDataFailureException("Update Last Watched Time")

        // When, Then
        assertThrows<RetrievingDataFailureException> {
            manageWatchingHistoryUseCase.updateLastWatchedTime(username, mediaId, mediaType)
        }
    }

    @Test
    fun `getWatchingHistory() should filter by movie type correctly`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.MOVIE
        val movieHistory = listOf(
            dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE),
            dummyHistoryItem.copy(id = 2, mediaType = MediaType.MOVIE)
        )
        coEvery { historyRepository.getWatchingHistory(username, mediaType) } returns flowOf(movieHistory)

        // When
        val result = manageWatchingHistoryUseCase.getWatchingHistory(username, mediaType).first()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.all { it.mediaType == MediaType.MOVIE }).isTrue()
    }

    @Test
    fun `getWatchingHistory() should filter by TV series type correctly`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.TV_SERIES
        val tvHistory = listOf(
            dummyHistoryItem.copy(id = 3, mediaType = MediaType.TV_SERIES),
            dummyHistoryItem.copy(id = 4, mediaType = MediaType.TV_SERIES)
        )
        coEvery { historyRepository.getWatchingHistory(username, mediaType) } returns flowOf(tvHistory)

        // When
        val result = manageWatchingHistoryUseCase.getWatchingHistory(username, mediaType).first()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.all { it.mediaType == MediaType.TV_SERIES }).isTrue()
    }

    @Test
    fun `getWatchingHistory() should return all types when mediaType is null`() = runTest {
        // Given
        val username = "testuser"
        val allHistory = listOf(
            dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE),
            dummyHistoryItem.copy(id = 2, mediaType = MediaType.TV_SERIES),
            dummyHistoryItem.copy(id = 3, mediaType = MediaType.MOVIE)
        )
        coEvery { historyRepository.getWatchingHistory(username, null) } returns flowOf(allHistory)

        // When
        val result = manageWatchingHistoryUseCase.getWatchingHistory(username, null).first()

        // Then
        assertThat(result).hasSize(3)
        assertThat(result.any { it.mediaType == MediaType.MOVIE }).isTrue()
        assertThat(result.any { it.mediaType == MediaType.TV_SERIES }).isTrue()
    }

    companion object {
        private val dummyGenre = Genre(id = 28, name = "Action")
        private val dummyHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(dummyGenre),
            isSaved = false
        )
        private val givenWatchingHistoryList = listOf(
            dummyHistoryItem,
            dummyHistoryItem.copy(id = 2),
            dummyHistoryItem.copy(id = 3)
        )
    }
} 