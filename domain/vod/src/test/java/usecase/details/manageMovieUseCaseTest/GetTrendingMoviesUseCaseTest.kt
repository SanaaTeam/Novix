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
import usecase.manageMovieUseCase.GetTrendingMoviesUseCase


class GetTrendingMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase

    @BeforeEach
    fun setUp() {
        getTrendingMoviesUseCase = GetTrendingMoviesUseCase(movieRepository)
    }

    @Test
    fun `getTrendingMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery { movieRepository.getTrendingMovies(1, dummyGenre.id) } returns expected

        val result = getTrendingMoviesUseCase(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTrendingMovies should throw when repository fails`() = runTest {
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery {
            movieRepository.getTrendingMovies(
                1, dummyGenre.id
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getTrendingMoviesUseCase(1, dummyGenre.id)
        }
    }
}