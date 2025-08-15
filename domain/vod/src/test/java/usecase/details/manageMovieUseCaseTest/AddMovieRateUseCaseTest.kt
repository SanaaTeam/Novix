package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.MovieRepository
import usecase.manageMovieUseCase.AddMovieRateUseCase


class AddMovieRateUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var addMovieRateUseCase: AddMovieRateUseCase

    @BeforeEach
    fun setUp() {
        addMovieRateUseCase = AddMovieRateUseCase(movieRepository)
    }

    @Test
    fun `addMovieRate should return true when rating is successful`() = runTest {
        val movieId = 201
        val rating = 7.5f

        coEvery { movieRepository.addMovieRate(movieId, rating) } returns true

        val result = addMovieRateUseCase(movieId, rating)

        assertThat(result).isTrue()
    }

    @Test
    fun `addMovieRate should return false when rating fails`() = runTest {
        val movieId = 202
        val rating = 5.0f

        coEvery { movieRepository.addMovieRate(movieId, rating) } returns false

        val result = addMovieRateUseCase(movieId, rating)

        assertThat(result).isFalse()
    }
}