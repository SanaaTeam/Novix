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
import search.usecase.search_param.SearchMovieOutput

class SearchUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchMoviesUseCase: SearchUseCase

    @BeforeEach
    fun setUp() {
        searchMoviesUseCase = SearchUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `searchMovies() should call addSearchHistory() from SearchHistoryRepository when search a movie`() =
        runTest {
            // Given
            val query = "Movie"
            val page = 1

            // When
            searchMoviesUseCase.searchMovies(query, page, null)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `searchMovies() should return movie search result when search without filters`() =
        runTest {
            // Given
            val query = "Movie"
            val filters = null
            val page = 1
            coEvery {
                searchRepository.searchMovies(query, page, filters)
            } returns searchMovieOutputList

            // When
            val result = searchMoviesUseCase.searchMovies(query, page, filters)

            // Then
            assertThat(result).isEqualTo(searchMovieOutputList)
        }

    @Test
    fun `searchMovies() should return movie search result when search with filters`() =
        runTest {
            // Given
            val query = "Movie"
            val page = 1
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchMovies(query, page, filters)
            } returns searchMovieOutputList

            // When
            val result = searchMoviesUseCase.searchMovies(query, page, filters)

            // Then
            assertThat(result).isEqualTo(searchMovieOutputList)
        }

    @Test
    fun `searchMovies() should throw RetrievingDataFailureException when try to search a movie failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchMovies(query, page, null)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchMoviesUseCase.searchMovies(query, page, null)
            }
        }

    private companion object {
         val searchMovieOutputList = listOf(
            SearchMovieOutput(
                id = 1,
                title = "title",
                posterImageUrl = "imageUrl",
            )
        )
    }
}