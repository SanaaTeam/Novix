package usecase

import com.google.common.truth.Truth.assertThat
import entity.Language
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.MediaFilters
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
    fun `execute() should add SearchHistoryItem and return movie search result without filters`() =
        runTest {
            // Given
            val query = "Movie"
            val language = Language.ARABIC
            val filters = null
            coEvery {
                searchRepository.searchMovies(
                    query,
                    filters,
                    language
                )
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters, language)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistoryItem(any())
            }
            assertThat(result).isEqualTo(searchMediaOutputList)
        }

    @Test
    fun `execute() should add SearchHistoryItem and return movie search result with filters`() =
        runTest {
            // Given
            val query = "Movie"
            val language = Language.ARABIC
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchMovies(
                    query,
                    filters,
                    language
                )
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters, language)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistoryItem(any())
            }
            assertThat(result).isEqualTo(searchMediaOutputList)
        }

    companion object {
        private val searchMediaOutputList = listOf(
            SearchMediaOutput(
                id = 1L,
                title = "title",
                posterImageUrl = "imageUrl",
                isSaved = true
            )
        )
    }
}