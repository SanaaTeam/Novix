package usecase

import com.google.common.truth.Truth.assertThat
import entity.Language
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import repository.SearchRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchActorsUseCase: SearchActorsUseCase

    @BeforeEach
    fun setUp() {
        searchActorsUseCase = SearchActorsUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should add SearchHistoryItem and return actor search result`() =
        runTest {
            // Given
            val query = "Actor"
            val language = Language.ARABIC
            coEvery { searchRepository.searchActors(query, language) } returns searchActorOutputList


            // When
            val result = searchActorsUseCase.execute(query, language)

            // Then
            coVerifyOrder {
                searchHistoryRepository.addSearchHistoryItem(any())
            }
            assertThat(result).isEqualTo(searchActorOutputList)
        }

    companion object {
        private val searchActorOutputList = listOf(
            SearchActorOutput(
                id = 1L,
                name = "title",
                profileImageUrl = "imgUrl",
            )
        )
    }
}