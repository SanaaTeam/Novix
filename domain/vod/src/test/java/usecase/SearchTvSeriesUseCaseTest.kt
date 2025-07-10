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

class SearchTvSeriesUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private lateinit var searchTvSeriesUseCase: SearchTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        searchTvSeriesUseCase = SearchTvSeriesUseCase(searchRepository)
    }

    @Test
    fun `execute() should return tv series search result without filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = null
            coEvery {
                searchRepository.searchTvSeries(query, filters)
            } returns searchMediaOutputList

            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

            // Then
            assertThat(result).isEqualTo(searchMediaOutputList)

        }

    @Test
    fun `execute() should return tv series search result with filters`() =
        runTest {
            // Given
            val query = "Tv Series"
            val filters = MediaFilters()
            coEvery {
                searchRepository.searchTvSeries(query, filters)
            } returns searchMediaOutputList


            // When
            val result = searchTvSeriesUseCase.execute(query, filters)

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