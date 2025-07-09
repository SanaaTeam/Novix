package usecase

import entity.Language
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import repository.SearchRepository

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
            val language = Language.ARABIC

            // When
            searchMoviesUseCase.execute(query, null, language)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistoryItem(any())
                searchRepository.searchMovies(any(), any(), any())
            }
        }
}