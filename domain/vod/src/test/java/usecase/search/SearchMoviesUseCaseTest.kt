package usecase.search

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
import search.usecase.SearchMoviesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchMovieOutput

class SearchMoviesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @BeforeEach
    fun setUp() {
        searchMoviesUseCase = SearchMoviesUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call addSearchHistory() from SearchHistoryRepository when search a movie`() =
        runTest {
            // Given
            val query = "Movie"

            // When
            searchMoviesUseCase.execute(query, null)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `execute() should return movie search result when search without filters`() =
        runTest {
            // Given
            val query = "Movie"
            val filters = null
            coEvery {
                searchRepository.searchMovies(query, filters)
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)
        }

    @Test
    fun `execute() should return movie search result when search with filters`() =
        runTest {
            // Given
            val query = "Movie"
            val filters = mediaFilters
            coEvery {
                searchRepository.searchMovies(query, filters)
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)
        }

    @Test
    fun `execute() should throw RetrievingDataFailureException when try to search a movie failed`() =
        runTest {
            // Given
            val query = "Sam"
            coEvery {
                searchRepository.searchMovies(query, null)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchMoviesUseCase.execute(query, null)
            }
        }

    private companion object {
        private val searchMediaOutputList = listOf(
            SearchMovieOutput(
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