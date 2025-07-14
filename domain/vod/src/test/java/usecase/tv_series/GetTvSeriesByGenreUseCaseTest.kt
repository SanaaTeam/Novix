package usecase.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesByGenreUseCase
import entity.Genre
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvSeriesByGenreUseCaseTest {
    private lateinit var getTvSeriesByGenreUseCase: GetTvSeriesByGenreUseCase
    private val repository: TvSeriesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getTvSeriesByGenreUseCase = GetTvSeriesByGenreUseCase(repository)
    }

    @Test
    fun `execute() should return a list of tv series when genre is provided`() = runTest {
        // Given
        coEvery { repository.getTvSeriesByGenre(any()) } returns listOf(dummyTvSeries)
        // When
        val result = getTvSeriesByGenreUseCase.execute(Genre.WAR_AND_POLITICS)
        // Then
        assert(result.isNotEmpty())
    }


    @Test
    fun `execute() should return an empty list when no tv series are found for the given genre`() =
        runTest {
            // Given
            coEvery { repository.getTvSeriesByGenre(any()) } returns emptyList()
            // When
            val result = getTvSeriesByGenreUseCase.execute(Genre.WAR_AND_POLITICS)
            // Then
            assert(result.isEmpty())
        }
}