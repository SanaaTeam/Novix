package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.MovieRepository
import usecase.manageMovieUseCase.GetMovieRateUseCase


class GetMovieRateUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieRateUseCase: GetMovieRateUseCase

    @BeforeEach
    fun setUp() {
        getMovieRateUseCase = GetMovieRateUseCase(movieRepository)
    }

    @Test
    fun `getMovieRate should return movie rating when available`() = runTest {
        val accountId = 1L
        val movieId = 101
        val expectedRating = 8

        coEvery { movieRepository.getMovieRate(accountId, movieId) } returns expectedRating

        val result = getMovieRateUseCase(accountId, movieId)

        assertThat(result).isEqualTo(expectedRating)
    }

    @Test
    fun `getMovieRate should return 0 when repository returns null`() = runTest {
        val accountId = 1L
        val movieId = 102

        coEvery { movieRepository.getMovieRate(accountId, movieId) } returns null

        val result = getMovieRateUseCase(accountId, movieId)

        assertThat(result).isEqualTo(0)
    }
}