package usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchMoviesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @BeforeEach
    fun setUp() {
        searchMoviesUseCase = SearchMoviesUseCase(searchRepository)
    }

    @Test
    fun `execute() should return movie search result without filters`() =
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
    fun `execute() should return movie search result with filters`() =
        runTest {
            // Given
            val query = "Movie"
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchMovies(query, filters)
            } returns searchMediaOutputList

            // When
            val result = searchMoviesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)
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