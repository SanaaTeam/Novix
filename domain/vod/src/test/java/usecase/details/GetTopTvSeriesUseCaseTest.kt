package usecase.details

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetTopTvSeriesUseCase
import entity.*
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopTvSeriesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getTopTvSeriesUseCase: GetTopTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        getTopTvSeriesUseCase = GetTopTvSeriesUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getTopTvSeries() from ActorRepository`() = runTest {
        // Given
        val actorId = 31

        // When
        getTopTvSeriesUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getTopTvSeries(actorId) }
    }

    @Test
    fun `execute() should return TV-series list when repository succeeds`() = runTest {
        // Given
        val actorId = 17
        val expected = dummySeries
        coEvery { actorRepository.getTopTvSeries(actorId) } returns expected

        // When
        val result = getTopTvSeriesUseCase.execute(actorId)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when repository fails`() = runTest {
        // Given
        val actorId = 404
        coEvery {
            actorRepository.getTopTvSeries(actorId)
        } throws RetrievingDataFailureException("Service unavailable")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getTopTvSeriesUseCase.execute(actorId)
        }
    }

    companion object {
        // Re-usable genres
        private val sciFi = Genre.SCIENCE_FICTION
        private val crime  = Genre.CRIME

        // Episodes
        private val s1e1 = Episode(
            id = 1,
            title = "Pilot",
            description = "The story begins.",
            durationMinutes = 52,
            releaseDate = LocalDate(2021, 9, 10)
        )
        private val s1e2 = Episode(
            id = 2,
            title = "Into the Unknown",
            description = "Mysteries deepen.",
            durationMinutes = 50,
            releaseDate = LocalDate(2021, 9, 17)
        )
        private val season1 = Season(
            id = 11,
            title = "Season 1",
            number = 1,
            episodes = listOf(s1e1, s1e2)
        )

        private val c1e1 = Episode(
            id = 3,
            title = "Dark Streets",
            description = "A crime is uncovered.",
            durationMinutes = 48,
            releaseDate = LocalDate(2020, 2, 1)
        )
        private val seasonCrime1 = Season(
            id = 21,
            title = "Season 1",
            number = 1,
            episodes = listOf(c1e1)
        )

        // Dummy TV-series list returned by the repository
        private val dummySeries = listOf(
            TvSeries(
                id = 101,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series1.jpg",
                title = "Future Worlds",
                description = "High-concept science-fiction drama.",
                seasons = listOf(season1),
                actorIds = listOf(17, 19),
                releaseDate = LocalDate(2021, 9, 10),
                genres = listOf(sciFi),
                imdbRating = 8.9f
            ),
            TvSeries(
                id = 102,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series2.jpg",
                title = "City Shadows",
                description = "Gritty crime thriller.",
                seasons = listOf(seasonCrime1),
                actorIds = listOf(17),
                releaseDate = LocalDate(2020, 2, 1),
                genres = listOf(crime),
                imdbRating = 8.5f
            )
        )
    }
}
