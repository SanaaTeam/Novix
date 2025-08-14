package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetActorTopMoviesUseCase
import com.google.common.truth.Truth.assertThat
import entity.Genre
import entity.Movie
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository
import kotlin.time.Duration.Companion.minutes


class GetActorTopMoviesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorTopMoviesUseCase: GetActorTopMoviesUseCase

    @BeforeEach
    fun setUp() {
        getActorTopMoviesUseCase = GetActorTopMoviesUseCase(actorRepository)
    }

    @Test
    fun `getActorTopMovies should call repository and return list`() = runTest {
        val actorId = 3
        coEvery { actorRepository.getActorTopMovies(actorId) } returns dummyMovies
        val result = getActorTopMoviesUseCase(actorId)
        assertThat(result).isEqualTo(dummyMovies)
    }

    @Test
    fun `getActorTopMovies should throw when repository fails`() = runTest {
        val actorId = 4
        coEvery { actorRepository.getActorTopMovies(actorId) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
            getActorTopMoviesUseCase(actorId)
        }
    }

    companion object {
        private val action = Genre(id = 1, name = "Action")
        private val drama = Genre(id = 2, name = "Drama")
        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster1.jpg",
                title = "Blockbuster One",
                genres = listOf(action),
                imdbRating = 8.6f,
                duration = 137.minutes,
                releaseDate = LocalDate(2023, 5, 12),
                overview = "A big summer action film.",
                trailerUrl = "",
                rating = 1,
            ),
            Movie(
                id = 2,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster2.jpg",
                title = "Critically-Acclaimed Drama",
                genres = listOf(drama),
                imdbRating = 8.2f,
                duration = 126.minutes,
                releaseDate = LocalDate(2022, 11, 3),
                overview = "An award-winning character study.",
                rating = 0,
                trailerUrl = ""
            )
        )
    }
}