package usecase.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesByGenreUseCase
import entity.Genre
import entity.TvSeries
import extensions.now
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
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

    val dummyTvSeries =
        TvSeries(
            id = 1,
            title = "The Walking Dead",
            overview = "",
            releaseDate = LocalDateTime.now().date,
            genres = listOf(Genre.WAR_AND_POLITICS),
            imdbRating = 1.2f,
            posterImageUrl = "Image",
            seasonsCount = 5,
            trailerUrl = "Url",
        )
}