package usecases.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetMovieImagesUseCase
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetMovieImagesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieImagesUseCase: GetMovieImagesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMovieImagesUseCase = GetMovieImagesUseCase(movieRepository)
    }

    @Test
    fun `execute() should return image list when movie images are available`() = runTest {
        // Given
        val movieId = 1
        val expected = listOf("image1.jpg", "image2.jpg")
        coEvery { movieRepository.getImages(movieId) } returns expected

        // When
        val result = getMovieImagesUseCase.execute(movieId)

        // Then
        assertEquals(expected, result)
        coVerify { movieRepository.getImages(movieId) }
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when getting images fails`() = runTest {
        // Given
        val movieId = 100
        val exception = RetrievingDataFailureException("Failed to get images")
        coEvery { movieRepository.getImages(movieId) } throws exception

        // When, Then
        assertThrows<RetrievingDataFailureException> {
            getMovieImagesUseCase.execute(movieId)
        }
        coVerify { movieRepository.getImages(movieId) }
    }
}
