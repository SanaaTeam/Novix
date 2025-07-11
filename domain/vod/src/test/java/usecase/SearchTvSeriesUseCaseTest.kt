package usecase

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchMediaOutput

class SearchTvSeriesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchTvSeriesUseCase: SearchTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        searchTvSeriesUseCase = SearchTvSeriesUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call addSearchHistory() from SearchHistoryRepository when search a tv series`() =
        runTest {
            // Given
            val query = "TvSeries"

            // When
            searchTvSeriesUseCase.execute(query, null)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `execute() should return tv series search result when search without filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = null
            coEvery {
                searchRepository.searchMedia(query, filters, MediaType.TV_SERIES)
            } returns searchMediaOutputList

            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)

        }

    @Test
    fun `execute() should return tv series search result when search with filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchMedia(query, filters, MediaType.TV_SERIES)
            } returns searchMediaOutputList


            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)
        }


    @Test
    fun `execute() should throw RetrievingDataFailureException when try to search an tv series failed`() =
        runTest {
            // Given
            val query = "Sam"
            coEvery {
                searchRepository.searchMedia(query, null, MediaType.TV_SERIES)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchTvSeriesUseCase.execute(query, null)
            }
        }

    companion object {
        private val searchMediaOutputList = listOf(
            SearchMediaOutput(
                id = 1,
                title = "title",
                posterImageUrl = "imageUrl",
                isSaved = true
            )
        )
    }
}