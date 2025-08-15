package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetMovieTrailerUseCase


class GetMovieTrailerUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieTrailerUseCase: GetMovieTrailerUseCase

    @BeforeEach
    fun setUp() {
        getMovieTrailerUseCase = GetMovieTrailerUseCase(movieRepository)
    }

    @Test
    fun `getMovieTrailer should return trailer url when available`() = runTest {
        val movieId = 4
        val expected = "trailer.mp4"
        coEvery { movieRepository.getMovieTrailer(movieId) } returns expected

        val result = getMovieTrailerUseCase(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieTrailer should return null when no trailer`() = runTest {
        val movieId = 5
        coEvery { movieRepository.getMovieTrailer(movieId) } returns null

        val result = getMovieTrailerUseCase(movieId)

        assertThat(result).isNull()
    }

    @Test
    fun `getMovieTrailer should throw when repository fails`() = runTest {
        val movieId = 6
        coEvery { movieRepository.getMovieTrailer(movieId) } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getMovieTrailerUseCase(movieId)
        }
    }
}