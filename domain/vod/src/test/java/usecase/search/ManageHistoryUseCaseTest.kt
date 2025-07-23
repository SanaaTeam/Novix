package usecase.search

import com.google.common.truth.Truth.assertThat
import entity.Genre
import exceptions.FailedToDeleteException
import exceptions.RetrievingDataFailureException
import extensions.now
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.HistoryRepository
import usecase.history.ManageHistoryUseCase
import usecase.history.history_param.SearchHistory

class ManageHistoryUseCaseTest {
    private var searchHistoryRepository: HistoryRepository = mockk(relaxed = true)
    private lateinit var manageHistoryUseCase: ManageHistoryUseCase

    @BeforeEach
    fun setUp() {
        manageHistoryUseCase = ManageHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `getSearchHistory() should return history list when available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory(any()) } returns flowOf(
                SearchHistoryList
            )

            // When
            val result = manageHistoryUseCase.getSearchHistory().single()

            // Then
            assertThat(result).isEqualTo(SearchHistoryList)
        }

    @Test
    fun `getSearchHistory() should return empty list when there are no search history available`() =
        runTest {
            // Given
            coEvery { searchHistoryRepository.getSearchHistory(any()) } returns flowOf(emptyList())

            // When
            val result = manageHistoryUseCase.getSearchHistory().single()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getSearchHistory() should throw NotFoundException when when try to search in the history failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.getSearchHistory(any())
            } throws RetrievingDataFailureException("Search History")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                manageHistoryUseCase.getSearchHistory()
            }
        }



    @Test
    fun `removeSearchHistory() should call removeSearchHistoryById() from SearchHistoryRepository when remove item form the search history`() =
        runTest {
            // Given
            val id = 1

            // When
            manageHistoryUseCase.removeSearchHistory(id)

            // Then
            coVerify {
                searchHistoryRepository.removeSearchHistoryById(id)
            }
        }

    @Test
    fun `removeSearchHistory() should throw FailedToDeleteException when try to clear recent viewed failed`() =
        runTest {
            // Given
            val id = 1
            coEvery {
                searchHistoryRepository.removeSearchHistoryById(id)
            } throws FailedToDeleteException("Search History")

            // When, Then
            assertThrows<FailedToDeleteException> {
                manageHistoryUseCase.removeSearchHistory(id)
            }
        }

    @Test
    fun `clearSearchHistory() should call clearSearchHistory() from SearchHistoryRepository when clear searched history`() =
        runTest {
            // When
            manageHistoryUseCase.clearSearchHistory()

            // Then
            coVerify {
                searchHistoryRepository.clearSearchHistory()
            }
        }

    @Test
    fun `clearSearchHistory() should throw FailedToDeleteException when try to clear searched history failed`() =
        runTest {
            // Given
            coEvery {
                searchHistoryRepository.clearSearchHistory()
            } throws FailedToDeleteException("Search History")

            // When, Then
            assertThrows<FailedToDeleteException> {
                manageHistoryUseCase.clearSearchHistory()
            }
        }

    @Test
    fun `getWatchedMoviesHistory should return movies when available`() = runTest {
        // Given
        val genre = Genre.ACTION
        val expectedMovies = listOf(mockk<entity.Movie>())
        coEvery { searchHistoryRepository.getWatchedMoviesHistory(1, genre) } returns expectedMovies

        // When
        val result = manageHistoryUseCase.getWatchedMoviesHistory(1, genre)

        // Then
        assertThat(result).isEqualTo(expectedMovies)
    }

    @Test
    fun `getWatchedMoviesHistory should return empty list when none available`() = runTest {
        // Given
        val genre = Genre.ACTION
        coEvery { searchHistoryRepository.getWatchedMoviesHistory(1, genre) } returns emptyList()

        // When
        val result = manageHistoryUseCase.getWatchedMoviesHistory(1, genre)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWatchedMoviesHistory should throw when repository fails`() = runTest {
        // Given
        val genre = Genre.ACTION
        coEvery {
            searchHistoryRepository.getWatchedMoviesHistory(1, genre)
        } throws RetrievingDataFailureException("Watched Movies")

        // When, Then
        assertThrows<RetrievingDataFailureException> {
            manageHistoryUseCase.getWatchedMoviesHistory(1, genre)
        }
    }

    @Test
    fun `getWatchedSeriesHistory should return series when available`() = runTest {
        // Given
        val genre = Genre.DRAMA
        val expectedSeries = listOf(mockk<entity.TvSeries>())
        coEvery { searchHistoryRepository.getWatchedSeriesHistory(1, genre) } returns expectedSeries

        // When
        val result = manageHistoryUseCase.getWatchedSeriesHistory(1, genre)

        // Then
        assertThat(result).isEqualTo(expectedSeries)
    }

    @Test
    fun `getWatchedSeriesHistory should return empty list when none available`() = runTest {
        // Given
        val genre = Genre.DRAMA
        coEvery { searchHistoryRepository.getWatchedSeriesHistory(1, genre) } returns emptyList()

        // When
        val result = manageHistoryUseCase.getWatchedSeriesHistory(1, genre)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWatchedSeriesHistory should throw when repository fails`() = runTest {
        // Given
        val genre = Genre.DRAMA
        coEvery {
            searchHistoryRepository.getWatchedSeriesHistory(1, genre)
        } throws RetrievingDataFailureException("Watched Series")

        // When, Then
        assertThrows<RetrievingDataFailureException> {
            manageHistoryUseCase.getWatchedSeriesHistory(1, genre)
        }
    }
    companion object {
        private val SearchHistoryList = listOf(
            SearchHistory(1, "Search Query 1", timestamp = LocalDateTime.now()),
            SearchHistory(2, "Search Query 2", timestamp = LocalDateTime.now()),
        )
    }
}