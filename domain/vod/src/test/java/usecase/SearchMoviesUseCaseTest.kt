package usecase

import com.google.common.truth.Truth.assertThat
import exceptions.FailedToDeleteException
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

class SearchMoviesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @BeforeEach
    fun setUp() {
        searchMoviesUseCase = SearchMoviesUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call searchMovies() from SearchRepository when search a movie`() =
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
                searchRepository.searchMedia(query, filters, MediaType.MOVIE)
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
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchMedia(query, filters, MediaType.MOVIE)
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)
        }

    @Test
    fun `execute() should throw FailedToDeleteException when try to clear search an movie failed`() =
        runTest {
            // Given
            val query = "Sam"
            coEvery {
                searchRepository.searchMedia(query, null, MediaType.MOVIE)
            } throws FailedToDeleteException("Actor")

            // When, Then
            assertThrows<FailedToDeleteException> {
                searchMoviesUseCase.execute(query, null)
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