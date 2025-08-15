package usecase.details.manageMovieUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Genre
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.manageMovieUseCase.GetMovieGenresUseCase


class GetMovieGenresUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var getMovieGenresUseCase: GetMovieGenresUseCase

    @BeforeEach
    fun setUp() {
        getMovieGenresUseCase = GetMovieGenresUseCase(movieRepository)
    }

    @Test
    fun `getMovieGenres should return genres when available`() = runTest {
        val dummyGenre = Genre(id = 1, name = "Action")
        val expected = listOf(dummyGenre)
        coEvery { movieRepository.getMovieGenres() } returns listOf(dummyGenre)

        val result = getMovieGenresUseCase()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieGenres should throw when repository fails`() = runTest {
        coEvery { movieRepository.getMovieGenres() } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            getMovieGenresUseCase()
        }
    }
}