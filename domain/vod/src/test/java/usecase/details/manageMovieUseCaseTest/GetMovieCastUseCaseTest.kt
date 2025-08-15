package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Actor
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetMovieCastUseCase


class GetMovieCastUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @BeforeEach
    fun setUp() {
        getMovieCastUseCase = GetMovieCastUseCase(movieRepository)
    }

    @Test
    fun `getMovieCast should return cast list when available`() = runTest {
        val movieId = 2
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery { movieRepository.getMovieCast(movieId) } returns expected

        val result = getMovieCastUseCase(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieCast should throw NovixAppException when repository fails`() = runTest {
        val movieId = 404
        coEvery { movieRepository.getMovieCast(movieId) } throws NovixAppException("Cast not found")

        assertThrows<NovixAppException> {
            getMovieCastUseCase(movieId)
        }
    }
}