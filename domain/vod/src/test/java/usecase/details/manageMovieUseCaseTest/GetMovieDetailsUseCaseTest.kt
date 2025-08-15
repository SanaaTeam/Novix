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
import usecase.manageMovieUseCase.GetMovieDetailsUseCase


class GetMovieDetailsUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @BeforeEach
    fun setUp() {
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)
    }

    @Test
    fun `getMovieDetails should return movie when found`() = runTest {
        val movieId = 1
        val expected = mockk<Movie>()
        coEvery { movieRepository.getMovieDetails(movieId) } returns expected

        val result = getMovieDetailsUseCase(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieDetails should throw NovixAppException when not found`() = runTest {
        val movieId = 404
        coEvery { movieRepository.getMovieDetails(movieId) } throws NovixAppException("Not found")

        assertThrows<NovixAppException> {
            getMovieDetailsUseCase(movieId)
        }
    }
}