package usecase.details

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import exceptions.NotFoundException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.MovieRepository
import usecase.ManageMovieUseCase

class ManageMovieUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private lateinit var manageMovieDetailsUseCase: ManageMovieUseCase

    @BeforeEach
    fun setUp() {
        manageMovieDetailsUseCase = ManageMovieUseCase(movieRepository)
    }

    @Test
    fun `getMovieDetails should return movie when found`() = runTest {
        val movieId = 1
        val expected = mockk<Movie>()
        coEvery { movieRepository.getMovieDetails(movieId) } returns expected

        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieDetails should throw NotFoundException when not found`() = runTest {
        val movieId = 404
        coEvery { movieRepository.getMovieDetails(movieId) } throws NotFoundException("Not found")

        assertThrows<NotFoundException> {
            manageMovieDetailsUseCase.getMovieDetails(movieId)
        }
    }

    @Test
    fun `getMovieCast should return cast list when available`() = runTest {
        val movieId = 2
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery { movieRepository.getMovieCast(movieId) } returns expected

        val result = manageMovieDetailsUseCase.getMovieCast(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieCast should throw NotFoundException when repository fails`() = runTest {
        val movieId = 404
        coEvery { movieRepository.getMovieCast(movieId) } throws NotFoundException("Cast not found")

        assertThrows<NotFoundException> {
            manageMovieDetailsUseCase.getMovieCast(movieId)
        }
    }

    @Test
    fun `getMovieImages should return images when available`() = runTest {
        val movieId = 3
        val expected = listOf("img1.jpg", "img2.jpg")
        coEvery { movieRepository.getImageUrls(movieId, 10) } returns expected

        val result = manageMovieDetailsUseCase.getMovieImages(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieImages should throw RetrievingDataFailureException when fails`() = runTest {
        val movieId = 100
        coEvery {
            movieRepository.getImageUrls(
                movieId,
                10
            )
        } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMovieImages(movieId)
        }
    }

    @Test
    fun `getMoviesByCategory should return movies when available`() = runTest {
        val category = Genre.ACTION
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getMoviesByCategory(category) } returns expected

        val result = manageMovieDetailsUseCase.getMoviesByCategory(category)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMoviesByCategory should return empty list when none available`() = runTest {
        val category = Genre.DRAMA
        coEvery { movieRepository.getMoviesByCategory(category) } returns emptyList()

        val result = manageMovieDetailsUseCase.getMoviesByCategory(category)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCategory should throw when repository fails`() = runTest {
        val category = Genre.COMEDY
        coEvery { movieRepository.getMoviesByCategory(category) } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMoviesByCategory(category)
        }
    }

    @Test
    fun `getMovieTrailer should return trailer url when available`() = runTest {
        val movieId = 4
        val expected = "trailer.mp4"
        coEvery { movieRepository.getMovieTrailer(movieId) } returns expected

        val result = manageMovieDetailsUseCase.getMovieTrailer(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieTrailer should return null when no trailer`() = runTest {
        val movieId = 5
        coEvery { movieRepository.getMovieTrailer(movieId) } returns null

        val result = manageMovieDetailsUseCase.getMovieTrailer(movieId)

        assertThat(result).isNull()
    }

    @Test
    fun `getMovieTrailer should throw when repository fails`() = runTest {
        val movieId = 6
        coEvery { movieRepository.getMovieTrailer(movieId) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMovieTrailer(movieId)
        }
    }

    @Test
    fun `getReviewsByMovieId should return reviews when available`() = runTest {
        val movieId = 7
        val expected = listOf(mockk<Review>(), mockk<Review>())
        coEvery { movieRepository.getReviewsByMovieId(movieId) } returns expected

        val result = manageMovieDetailsUseCase.getReviewsByMovieId(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getReviewsByMovieId should return empty list when none available`() = runTest {
        val movieId = 8
        coEvery { movieRepository.getReviewsByMovieId(movieId) } returns emptyList()

        val result = manageMovieDetailsUseCase.getReviewsByMovieId(movieId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getReviewsByMovieId should throw when repository fails`() = runTest {
        val movieId = 9
        coEvery { movieRepository.getReviewsByMovieId(movieId) } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getReviewsByMovieId(movieId)
        }
    }

    @Test
    fun `getSimilarMoviesByMovieId should return similar movies when available`() = runTest {
        val movieId = 10
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } returns expected

        val result = manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSimilarMoviesByMovieId should return empty list when none available`() = runTest {
        val movieId = 11
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } returns emptyList()

        val result = manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSimilarMoviesByMovieId should throw when repository fails`() = runTest {
        val movieId = 12
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId) } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId)
        }
    }

    @Test
    fun `getPopularMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getPopularMovies() } returns expected

        val result = manageMovieDetailsUseCase.getPopularMovies()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getPopularMovies should throw when repository fails`() = runTest {
        coEvery { movieRepository.getPopularMovies() } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getPopularMovies()
        }
    }

    @Test
    fun `getTopRatedMovies should return movies when available`() = runTest {
        val genre = Genre.ACTION
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getTopRatedMovies(genre) } returns expected

        val result = manageMovieDetailsUseCase.getTopRatedMovies(genre)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTopRatedMovies should throw when repository fails`() = runTest {
        val genre = Genre.ACTION
        coEvery { movieRepository.getTopRatedMovies(genre) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getTopRatedMovies(genre)
        }
    }

    @Test
    fun `getTrendingMovies should return movies when available`() = runTest {
        val genre = Genre.DRAMA
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getTrendingMovies(genre) } returns expected

        val result = manageMovieDetailsUseCase.getTrendingMovies(genre)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTrendingMovies should throw when repository fails`() = runTest {
        val genre = Genre.DRAMA
        coEvery { movieRepository.getTrendingMovies(genre) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getTrendingMovies(genre)
        }
    }

    @Test
    fun `getUpcomingMovies should return movies when available`() = runTest {
        val genre = Genre.COMEDY
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getUpcomingMovies(genre) } returns expected

        val result = manageMovieDetailsUseCase.getUpcomingMovies(genre)

        assertThat(result).isEqualTo(expected)
    }



    @Test
    fun `getUpcomingMovies should throw when repository fails`() = runTest {
        val genre = Genre.COMEDY
        coEvery { movieRepository.getUpcomingMovies(genre) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getUpcomingMovies(genre)
        }
    }

    @Test
    fun `getMovieGenres should return genres when available`() = runTest {
        val expected = listOf(Genre.ACTION, Genre.DRAMA)
        coEvery { movieRepository.getMovieGenres() } returns expected

        val result = manageMovieDetailsUseCase.getMovieGenres()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieGenres should throw when repository fails`() = runTest {
        coEvery { movieRepository.getMovieGenres() } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMovieGenres()
        }
    }
}