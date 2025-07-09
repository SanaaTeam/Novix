package usecase

import entity.Language
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import repository.SearchRepository

class SearchTvSeriesUseCaseTest  {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchTvSeriesUseCase: SearchTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        searchTvSeriesUseCase = SearchTvSeriesUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call searchTvSeries() from SearchRepository when search a tv series`() =
        runTest {
            // Given
            val query = "Movie"
            val language = Language.ARABIC

            // When
            searchTvSeriesUseCase.execute(query, null, language)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistoryItem(any())
                searchRepository.searchTvSeries(any(), any(), any())
            }
        }
}