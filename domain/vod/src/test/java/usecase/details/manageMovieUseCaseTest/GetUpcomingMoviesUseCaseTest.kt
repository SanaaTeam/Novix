package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Genre
import entity.Movie
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetUpcomingMoviesUseCase


class GetUpcomingMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @BeforeEach
    fun setUp() {
        getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(movieRepository)
    }

    @Test
    fun `getUpcomingMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery { movieRepository.getUpcomingMovies(1, genreId = dummyGenre.id) } returns expected

        val result = getUpcomingMoviesUseCase(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `getUpcomingMovies should throw when repository fails`() = runTest {
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery {
            movieRepository.getUpcomingMovies(
                1, dummyGenre.id
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getUpcomingMoviesUseCase(1, dummyGenre.id)
        }
    }
}