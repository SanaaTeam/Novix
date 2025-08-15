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
import usecase.manageMovieUseCase.GetTopRatedMoviesUseCase


class GetTopRatedMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase

    @BeforeEach
    fun setUp() {
        getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase(movieRepository)
    }

    @Test
    fun `getTopRatedMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery { movieRepository.getTopRatedMovies(1, dummyGenre.id) } returns expected

        val result = getTopRatedMoviesUseCase(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTopRatedMovies should throw when repository fails`() = runTest {
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery {
            movieRepository.getTopRatedMovies(
                1, dummyGenre.id
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getTopRatedMoviesUseCase(1, dummyGenre.id)
        }
    }
}