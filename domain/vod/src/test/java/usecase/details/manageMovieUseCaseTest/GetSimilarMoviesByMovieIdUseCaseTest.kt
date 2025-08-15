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
import usecase.manageMovieUseCase.GetSimilarMoviesByMovieIdUseCase


class GetSimilarMoviesByMovieIdUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getSimilarMoviesByMovieIdUseCase: GetSimilarMoviesByMovieIdUseCase

    @BeforeEach
    fun setUp() {
        getSimilarMoviesByMovieIdUseCase = GetSimilarMoviesByMovieIdUseCase(movieRepository)
    }

    @Test
    fun `getSimilarMoviesByMovieId should return similar movies when available`() = runTest {
        val movieId = 10
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId, 1) } returns expected

        val result = getSimilarMoviesByMovieIdUseCase(movieId, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSimilarMoviesByMovieId should return empty list when none available`() = runTest {
        val movieId = 11
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId, 1) } returns emptyList()

        val result = getSimilarMoviesByMovieIdUseCase(movieId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSimilarMoviesByMovieId should throw when repository fails`() = runTest {
        val movieId = 12
        coEvery {
            movieRepository.getSimilarMoviesByMovieId(
                movieId, 1
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getSimilarMoviesByMovieIdUseCase(movieId, 1)
        }
    }
}