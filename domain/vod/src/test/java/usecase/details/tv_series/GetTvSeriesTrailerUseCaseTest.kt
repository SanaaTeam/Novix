package usecase.details.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesTrailerUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvSeriesTrailerUseCaseTest {
    private lateinit var getTvSeriesTrailerUseCase: GetTvSeriesTrailerUseCase
    private val tvSeriesRepository: TvSeriesRepository = mockk()

    @BeforeEach
    fun setUp() {
        getTvSeriesTrailerUseCase = GetTvSeriesTrailerUseCase(tvSeriesRepository)
    }

    @Test
    fun `should return trailer url`() = runTest {
        // Given
        val seriesId = 1
        val trailerUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } returns trailerUrl
        // When
        val result = getTvSeriesTrailerUseCase.execute(seriesId)
        // Then
        assert(result == trailerUrl)
    }

    @Test
    fun `should return null if no trailer found`() = runTest {
        // Given
        val seriesId = 1
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } returns null
        // When
        val result = getTvSeriesTrailerUseCase.execute(seriesId)
        // Then
        assert(result == null)
    }

    @Test
    fun `should throw exception if repository throws exception`() = runTest {
        // Given
        val seriesId = 1
        val exception = IllegalStateException("Error getting trailer")
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } throws exception
        // When & Then
        assertThrows<IllegalStateException> {
            getTvSeriesTrailerUseCase.execute(seriesId)
        }

    }

}