package usecase.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetMovieCastUseCase
import entity.Actor
import exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetMovieCastUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMovieCastUseCase = GetMovieCastUseCase(movieRepository)
    }

    @Test
    fun `execute() should return cast list when movie cast exists`() = runTest {
        // Given
        val movieId = 1
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery { movieRepository.getCast(movieId) } returns expected

        // When
        val result = getMovieCastUseCase.execute(movieId)

        // Then
        assertEquals(expected, result)
        coVerify { movieRepository.getCast(movieId) }
    }

    @Test
    fun `execute() should throw NotFoundException when movie cast not found`() = runTest {
        // Given
        val movieId = 404
        val exception = NotFoundException("Cast")
        coEvery { movieRepository.getCast(movieId) } throws exception

        // When, Then
        assertThrows<NotFoundException> {
            getMovieCastUseCase.execute(movieId)
        }
        coVerify { movieRepository.getCast(movieId) }
    }
}
