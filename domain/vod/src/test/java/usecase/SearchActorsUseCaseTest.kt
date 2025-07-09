package usecase

import entity.Language
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import repository.SearchRepository

class SearchActorsUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchActorsUseCase: SearchActorsUseCase

    @BeforeEach
    fun setUp() {
        searchActorsUseCase = SearchActorsUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call searchActors() from SearchRepository when search an actor`() =
        runTest {
            // Given
            val query = "Movie"
            val language = Language.ARABIC

            // When
            searchActorsUseCase.execute(query, language)

            // Then
            coVerifyOrder {
                searchHistoryRepository.addSearchHistoryItem(any())
                searchRepository.searchActors(any(), any())
            }
        }
}