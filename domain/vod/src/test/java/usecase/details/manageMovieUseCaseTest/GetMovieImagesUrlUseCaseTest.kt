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
import usecase.manageMovieUseCase.GetMovieImagesUrlUseCase


class GetMovieImagesUrlUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieImagesUrlUseCase: GetMovieImagesUrlUseCase

    @BeforeEach
    fun setUp() {
        getMovieImagesUrlUseCase = GetMovieImagesUrlUseCase(movieRepository)
    }

    @Test
    fun `getMovieImages should return images when available`() = runTest {
        val movieId = 3
        val expected = listOf("img1.jpg", "img2.jpg")
        coEvery { movieRepository.getImageUrls(movieId, 10) } returns expected

        val result = getMovieImagesUrlUseCase(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieImages should throw NovixAppException when fails`() = runTest {
        val movieId = 100
        coEvery {
            movieRepository.getImageUrls(
                movieId, 10
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getMovieImagesUrlUseCase(movieId)
        }
    }
}