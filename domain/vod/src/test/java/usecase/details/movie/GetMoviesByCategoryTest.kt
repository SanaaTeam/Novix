package usecase.details.movie

import com.google.common.truth.Truth.assertThat
import details.repository.MovieRepository
import details.usecase.movie.GetMoviesByCategory
import entity.Genre
import entity.Movie
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetMoviesByCategoryTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByCategory: GetMoviesByCategory

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMoviesByCategory = GetMoviesByCategory(movieRepository)
    }
    @Test
    fun `execute() should return movies when available`() = runTest {
        // Given
        val category = Genre.ACTION
        val movies = listOf(testMovie1, testMovie2)
        coEvery { movieRepository.getMoviesByCategory(category) } returns movies

        // When
        val result = getMoviesByCategory.execute(category)

        // Then
        assertThat(result).isEqualTo(movies)
    }
    @Test
    fun `execute() should return empty list when there are no movies in the category`() = runTest {
        // Given
        val category = Genre.DRAMA
        coEvery { movieRepository.getMoviesByCategory(category) } returns emptyList()

        // When
        val result = getMoviesByCategory.execute(category)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `execute() should throw exception when repository fails`() = runTest {
        // Given
        val category = Genre.COMEDY
        coEvery {
            movieRepository.getMoviesByCategory(category)
        } throws RetrievingDataFailureException("Failed to retrieve movies by category")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getMoviesByCategory.execute(category)
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