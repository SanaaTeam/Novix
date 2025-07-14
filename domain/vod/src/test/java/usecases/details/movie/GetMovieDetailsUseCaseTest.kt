package usecases.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetMovieDetailsUseCase
import entity.Movie
import exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetMovieDetailsUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)
    }

    @Test
    fun `execute() should return movie details when movie is found`() = runTest {
        // Given
        val movieId = 1
        val expectedMovie = mockk<Movie>()
        coEvery { movieRepository.getMovieDetails(movieId) } returns expectedMovie

        // When
        val result = getMovieDetailsUseCase.execute(movieId)

        // Then
        assertEquals(expectedMovie, result)
        coVerify { movieRepository.getMovieDetails(movieId) }
    }

    @Test
    fun `execute() should throw NotFoundException when movie is not found`() = runTest {
        // Given
        val movieId = 404
        val exception = NotFoundException("Movie")
        coEvery { movieRepository.getMovieDetails(movieId) } throws exception

        // When, Then
        assertThrows<NotFoundException> {
            getMovieDetailsUseCase.execute(movieId)
        }
        coVerify { movieRepository.getMovieDetails(movieId) }
    }
}
