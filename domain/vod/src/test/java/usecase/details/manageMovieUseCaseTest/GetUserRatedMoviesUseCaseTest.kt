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
import usecase.manageMovieUseCase.GetUserRatedMoviesUseCase

class GetUserRatedMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase

    @BeforeEach
    fun setUp() {
        getUserRatedMoviesUseCase = GetUserRatedMoviesUseCase(movieRepository)
    }

    @Test
    fun `getUserRatedMovies should return rated movies when available`() = runTest {
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getUserRatedMovies() } returns expected

        val result = getUserRatedMoviesUseCase()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getUserRatedMovies should return empty list when none rated`() = runTest {
        coEvery { movieRepository.getUserRatedMovies() } returns emptyList()

        val result = getUserRatedMoviesUseCase()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUserRatedMovies should throw exception when repository fails`() = runTest {
        coEvery { movieRepository.getUserRatedMovies() } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getUserRatedMoviesUseCase()
        }
    }
}