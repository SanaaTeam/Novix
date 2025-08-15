package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Genre
import entity.Movie
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetMoviesByCategoryUseCase


class GetMoviesByCategoryUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase

    @BeforeEach
    fun setUp() {
        getMoviesByCategoryUseCase = GetMoviesByCategoryUseCase(movieRepository)
    }

    @Test
    fun `getMoviesByCategory should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        val dummyGenre = Genre(id = 1, name = "Action")
        coEvery { movieRepository.getMoviesByCategory(dummyGenre.id, 1) } returns expected

        val result = getMoviesByCategoryUseCase(dummyGenre.id, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMoviesByCategory should return empty list when none available`() = runTest {
        val category = Genre(id = 1, name = "Action")
        coEvery { movieRepository.getMoviesByCategory(category.id, 1) } returns emptyList()

        val result = getMoviesByCategoryUseCase(category.id, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCategory should throw when repository fails`() = runTest {
        val category = Genre(id = 1, name = "Action")
        coEvery {
            movieRepository.getMoviesByCategory(
                category.id, 1
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getMoviesByCategoryUseCase(category.id, 1)
        }
    }
}