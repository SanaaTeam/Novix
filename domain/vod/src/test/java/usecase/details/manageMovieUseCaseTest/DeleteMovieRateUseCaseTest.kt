package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.MovieRepository
import usecase.manageMovieUseCase.DeleteMovieRateUseCase

class DeleteMovieRateUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var deleteMovieRateUseCase: DeleteMovieRateUseCase

    @BeforeEach
    fun setUp() {
        deleteMovieRateUseCase = DeleteMovieRateUseCase(movieRepository)
    }

    @Test
    fun `deleteMovieRate should return true when deletion is successful`() = runTest {
        val movieId = 101
        coEvery { movieRepository.deleteMovieRate(movieId) } returns true

        val result = deleteMovieRateUseCase(movieId)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteMovieRate should return false when deletion fails`() = runTest {
        val movieId = 102
        coEvery { movieRepository.deleteMovieRate(movieId) } returns false

        val result = deleteMovieRateUseCase(movieId)

        assertThat(result).isFalse()
    }
}