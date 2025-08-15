package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Review
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetReviewsByMovieIdUseCase


class GetReviewsByMovieIdUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getReviewsByMovieIdUseCase: GetReviewsByMovieIdUseCase

    @BeforeEach
    fun setUp() {
        getReviewsByMovieIdUseCase = GetReviewsByMovieIdUseCase(movieRepository)
    }

    @Test
    fun `getReviewsByMovieId should return reviews when available`() = runTest {
        val movieId = 7
        val expected = listOf(mockk<Review>(), mockk<Review>())
        coEvery { movieRepository.getReviewsByMovieId(movieId, 1) } returns expected

        val result = getReviewsByMovieIdUseCase(movieId, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getReviewsByMovieId should return empty list when none available`() = runTest {
        val movieId = 8
        coEvery { movieRepository.getReviewsByMovieId(movieId, 1) } returns emptyList()

        val result = getReviewsByMovieIdUseCase(movieId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getReviewsByMovieId should throw when repository fails`() = runTest {
        val movieId = 9
        coEvery {
            movieRepository.getReviewsByMovieId(
                movieId, 1
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getReviewsByMovieIdUseCase(movieId, 1)
        }
    }
}