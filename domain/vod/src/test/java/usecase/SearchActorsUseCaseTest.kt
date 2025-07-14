package usecase

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
import search.usecase.SearchActorsUseCase
import search.usecase.search_param.SearchActorOutput

class SearchActorsUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchActorsUseCase: SearchActorsUseCase

    @BeforeEach
    fun setUp() {
        searchActorsUseCase = SearchActorsUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `execute() should call addSearchHistory() from SearchHistoryRepository when search an actor`() =
        runTest {
            // Given
            val query = "Actor"

            // When
            searchActorsUseCase.execute(query)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }


    @Test
    fun `execute() should return actor search result when search`() =
        runTest {
            // Given
            val query = "Actor"
            coEvery { searchRepository.searchActors(query) } returns searchActorOutputList

            // When
            val result = searchActorsUseCase.execute(query)

            // Then
            assertThat(result).isEqualTo(searchActorOutputList)
        }


    @Test
    fun `execute() should throw RetrievingDataFailureException when try to search an actor failed`() =
        runTest {
            // Given
            val query = "Sam"
            coEvery {
                searchRepository.searchActors(query)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchActorsUseCase.execute(query)
            }
        }

    companion object {
        private val searchActorOutputList = listOf(
            SearchActorOutput(
                id = 1,
                name = "title",
                profileImageUrl = "imgUrl",
            )
        )
    }
}