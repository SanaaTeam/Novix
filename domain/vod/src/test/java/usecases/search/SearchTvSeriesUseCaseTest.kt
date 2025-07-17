package usecases.search

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import search.repository.SearchHistoryRepository
import search.repository.SearchRepository
import search.usecase.SearchTvSeriesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchTvSeriesOutput

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
                searchRepository.searchTvShows(query, filters)
            } returns searchTvShowsOutputList

            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchTvShowsOutputList)

        }

    @Test
    fun `execute() should return tv series search result when search with filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = mediaFilters
            coEvery {
                searchRepository.searchTvShows(query, filters)
            } returns searchTvShowsOutputList

            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchTvShowsOutputList)
        }


    @Test
    fun `execute() should throw RetrievingDataFailureException when try to search an tv series failed`() =
        runTest {
            // Given
            val query = "Sam"
            coEvery {
                searchRepository.searchTvShows(query, null)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchTvSeriesUseCase.execute(query, null)
            }
        }

    private companion object {
        private val searchTvShowsOutputList = listOf(
            SearchTvSeriesOutput(
                id = 1,
                title = "title",
                posterImageUrl = "imageUrl",
            )
        )
        val mediaFilters = MediaFilters(
            startYear = 1980,
            endYear = 2025,
            imdbRating = 5f
        )
    }
}