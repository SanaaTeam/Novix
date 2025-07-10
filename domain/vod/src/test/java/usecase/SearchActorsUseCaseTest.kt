package usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchRepository
import usecase.search.SearchActorOutput

class SearchActorsUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private lateinit var searchActorsUseCase: SearchActorsUseCase

    @BeforeEach
    fun setUp() {
        searchActorsUseCase = SearchActorsUseCase(searchRepository)
    }

    @Test
    fun `execute() should return actor search result`() =
        runTest {
            // Given
            val query = "Actor"
            coEvery { searchRepository.searchActors(query) } returns searchActorOutputList


            // When
            val result = searchActorsUseCase.execute(query)

            // Then
            assertThat(result).isEqualTo(searchActorOutputList)
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