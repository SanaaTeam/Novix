package usecase.details.actor

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetTopMoviesUseCase
import entity.Genre
import entity.Movie
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopMoviesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getTopMoviesUseCase: GetTopMoviesUseCase

    @BeforeEach
    fun setUp() {
        getTopMoviesUseCase = GetTopMoviesUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getTopMovies() from ActorRepository`() = runTest {
        // Given
        val actorId = 21

        // When
        getTopMoviesUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getTopMovies(actorId) }
    }

    @Test
    fun `execute() should return movie list when repository succeeds`() = runTest {
        // Given
        val actorId = 10
        val expected = dummyMovies
        coEvery { actorRepository.getTopMovies(actorId) } returns expected

        // When
        val result = getTopMoviesUseCase.execute(actorId)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when repository fails`() = runTest {
        // Given
        val actorId = 404
        coEvery {
            actorRepository.getTopMovies(actorId)
        } throws RetrievingDataFailureException("Service unavailable")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getTopMoviesUseCase.execute(actorId)
        }
    }

    companion object {
        private val action = Genre.ACTION
        private val drama  = Genre.DRAMA

        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster1.jpg",
                title = "Blockbuster One",
                genres = listOf(action),
                imdbRating = 8.6f,
                duration = 137,                       // minutes
                releaseDate = LocalDate(2023, 5, 12),
                overview = "A big summer action film."
            ),
            Movie(
                id = 2,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster2.jpg",
                title = "Critically-Acclaimed Drama",
                genres = listOf(drama),
                imdbRating = 8.2f,
                duration = 126,                       // minutes
                releaseDate = LocalDate(2022, 11, 3),
                overview = "An award-winning character study."
            )
        )
    }
}
