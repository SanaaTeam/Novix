package usecase.details.actor

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetActorTopTvSeriesUseCase
import entity.Episode
import entity.Genre
import entity.Season
import entity.TvSeries
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
    private lateinit var getTopTvSeriesUseCase: GetActorTopTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        getTopTvSeriesUseCase = GetActorTopTvSeriesUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getTopTvSeries() from ActorRepository`() = runTest {
        // Given
        val actorId = 31

        // When
        getTopTvSeriesUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getActorTopTvSeries(actorId) }
    }

    @Test
    fun `execute() should return TV-series list when repository succeeds`() = runTest {
        // Given
        val actorId = 17
        val expected = dummySeries
        coEvery { actorRepository.getActorTopTvSeries(actorId) } returns expected

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
            actorRepository.getActorTopTvSeries(actorId)
        } throws RetrievingDataFailureException("Service unavailable")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getTopTvSeriesUseCase.execute(actorId)
        }
    }

    companion object {
        private val sciFi = Genre.SCIENCE_FICTION
        private val crime = Genre.CRIME

        private val s1e1 = Episode(
            id = 1,
            title = "Pilot",
            number = 1,
            seasonNumber = 1,
            imdbRating = 8.1f,
            overview = "The story begins.",
            durationMinutes = 52,
            releaseDate = LocalDate(2021, 9, 10)
        )
        private val s1e2 = Episode(
            id = 2,
            title = "Into the Unknown",
            number = 2,
            seasonNumber = 1,
            imdbRating = 8.4f,
            overview = "Mysteries deepen.",
            durationMinutes = 50,
            releaseDate = LocalDate(2021, 9, 17)
        )
        private val season1 = Season(
            id = 11,
            title = "Season 1",
            overview = "The inaugural season of *Future Worlds*.",
            number = 1,
            episodes = emptyList()      // s1e1 + s1e2
        )

        private val c1e1 = Episode(
            id = 3,
            title = "Dark Streets",
            number = 1,
            seasonNumber = 1,
            imdbRating = 8.0f,
            overview = "A crime is uncovered.",
            durationMinutes = 48,
            releaseDate = LocalDate(2020, 2, 1)
        )
        private val seasonCrime1 = Season(
            id = 21,
            title = "Season 1",
            overview = "The first season of *City Shadows*.",
            number = 1,
            episodes = emptyList()     // c1e1
        )

        private val dummySeries = listOf(
            TvSeries(
                id = 101,
                title = "Future Worlds",
                overview = "High-concept science-fiction drama.",
                releaseDate = LocalDate(2021, 9, 10),
                genres = listOf(sciFi),
                imdbRating = 8.9f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series1.jpg",
                seasonsCount = 1,                       // season1
            ),
            TvSeries(
                id = 102,
                title = "City Shadows",
                overview = "Gritty crime thriller.",
                releaseDate = LocalDate(2020, 2, 1),
                genres = listOf(crime),
                imdbRating = 8.5f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series2.jpg",
                seasonsCount = 1,                       // seasonCrime1
            )
        )
    }
}
