package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Movie
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetPopularMoviesUseCase


class GetPopularMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    @BeforeEach
    fun setUp() {
        getPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository)
    }

    @Test
    fun `getPopularMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getPopularMovies(1) } returns expected

        val result = getPopularMoviesUseCase(1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getPopularMovies should throw when repository fails`() = runTest {
        coEvery { movieRepository.getPopularMovies(1) } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getPopularMoviesUseCase(1)
        }
    }
}
