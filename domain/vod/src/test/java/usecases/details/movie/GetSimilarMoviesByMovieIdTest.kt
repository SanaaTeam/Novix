package usecases.details.movie

import com.google.common.truth.Truth.assertThat
import details.repository.MovieRepository
import details.usecase.movie.GetSimilarMoviesByMovieId
import entity.Genre
import entity.Movie
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetSimilarMoviesByMovieIdTest{
    private lateinit var movieRepository: MovieRepository
    private lateinit var getSimilarMoviesByMovieId: GetSimilarMoviesByMovieId

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getSimilarMoviesByMovieId = GetSimilarMoviesByMovieId(movieRepository)
    }

    @Test
    fun `execute() should return similar movies when available`() = runTest {
        // Given
        val movieId = 1
        val similarMovies = listOf(testMovie1, testMovie2)
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } returns similarMovies

        // When
        val result = getSimilarMoviesByMovieId.execute(movieId)

        // Then
        assertThat(result).isEqualTo(similarMovies)
    }

    @Test
    fun `execute() should return empty list when there are no similar movies`() = runTest {
        // Given
        val movieId = 2
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } returns emptyList()

        // When
        val result = getSimilarMoviesByMovieId.execute(movieId)

        // Then
        assertThat(result).isEmpty()
    }
    @Test
    fun `execute() should throw exception when repository fails`() = runTest {
        // Given
        val movieId = 1
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } throws RetrievingDataFailureException("Failed to retrieve similar movies")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getSimilarMoviesByMovieId.execute(movieId)
        }
    }



    companion object {
        private val testMovie1 = Movie(
            id = 101,
            posterImageUrl = "https://image.tmdb.org/t/p/w500/image1.jpg",
            title = "Test Movie 1",
            genres = listOf(Genre.ACTION),
            imdbRating = 7.8f,
            duration = 120,
            releaseDate = LocalDate(2023, 5, 20),
            overview = "Overview of movie 1"
        )

        private val testMovie2 = Movie(
            id = 102,
            posterImageUrl = "https://image.tmdb.org/t/p/w500/image2.jpg",
            title = "Test Movie 2",
            genres = listOf(Genre.SCIENCE_FICTION, Genre.ACTION),
            imdbRating = 8.2f,
            duration = 130,
            releaseDate = LocalDate(2022, 8, 10),
            overview = "Overview of movie 2"
        )
    }

}