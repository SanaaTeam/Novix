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
import search.usecase.SearchUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchTvSeriesOutput

class SearchTvSeriesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchUseCase: SearchUseCase

    @BeforeEach
    fun setUp() {
        searchUseCase = SearchUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `searchTvShows() should call addSearchHistory() from SearchHistoryRepository when search a tv series`() =
        runTest {
            // Given
            val query = "TvSeries"
            val page = 1

            // When
            searchUseCase.searchTvShows(query, page, null)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `searchTvShows() should return tv series search result when search without filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val page = 1
            val filters = null
            coEvery {
                searchRepository.searchTvShows(query, page, filters)
            } returns searchTvShowsOutputList

            // When
            val result = searchUseCase.searchTvShows(query, page, filters)

            // Then
            assertThat(result).isEqualTo(searchTvShowsOutputList)

        }

    @Test
    fun `searchTvShows() should return tv series search result when search with filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = MediaFilters()
            val page = 1
            coEvery {
                searchRepository.searchTvShows(query, page, filters)
            } returns searchTvShowsOutputList

            // When
            val result = searchUseCase.searchTvShows(query, page, filters)

            // Then
            assertThat(result).isEqualTo(searchTvShowsOutputList)
        }


    @Test
    fun `searchTvShows() should throw RetrievingDataFailureException when try to search an tv series failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchTvShows(query, page, null)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchUseCase.searchTvShows(query, page, null)
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
    }
}