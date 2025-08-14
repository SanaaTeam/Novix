package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetActorTopTvShowsUseCase
import com.google.common.truth.Truth.assertThat
import entity.Genre
import entity.TvShow
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository

class GetActorTopTvShowsUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorTopTvShowsUseCase: GetActorTopTvShowsUseCase

    @BeforeEach
    fun setUp() {
        getActorTopTvShowsUseCase = GetActorTopTvShowsUseCase(actorRepository)
    }

    @Test
    fun `getActorTopTvShows should call repository and return list`() = runTest {
        val actorId = 5
        coEvery { actorRepository.getActorTopTvShows(actorId) } returns dummyTvShows
        val result = getActorTopTvShowsUseCase(actorId)
        assertThat(result).isEqualTo(dummyTvShows)
    }

    @Test
    fun `getActorTopTvShows should throw when repository fails`() = runTest {
        val actorId = 6
        coEvery { actorRepository.getActorTopTvShows(actorId) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
            getActorTopTvShowsUseCase(actorId)
        }
    }

    companion object {
        private val sciFi = Genre(id = 3, name = "Science Fiction")
        private val crime = Genre(id = 4, name = "Crime")
        private val dummyTvShows = listOf(
            TvShow(
                id = 101,
                title = "Future Worlds",
                overview = "High-concept science-fiction drama.",
                releaseDate = LocalDate(2021, 9, 10),
                genres = listOf(sciFi),
                imdbRating = 8.9f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series1.jpg",
                seasonsCount = 1,
                rating = 0
            ),
            TvShow(
                id = 102,
                title = "City Shadows",
                overview = "Gritty crime thriller.",
                releaseDate = LocalDate(2020, 2, 1),
                genres = listOf(crime),
                imdbRating = 8.5f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series2.jpg",
                seasonsCount = 1,
                rating = 0
            )
        )
    }
}