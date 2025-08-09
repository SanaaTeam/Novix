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
                movieId, 10
            )
        } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMovieImages(movieId)
        }
    }

    @Test
    fun `getMoviesByCategory should return movies when available`() = runTest {

        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getMoviesByCategory(dummyGenre.id, 1) } returns expected

        val result = manageMovieDetailsUseCase.getMoviesByCategory(dummyGenre.id, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMoviesByCategory should return empty list when none available`() = runTest {
        val category = Genre(
            id = 1, name = "Action"
        )
        coEvery { movieRepository.getMoviesByCategory(category.id, 1) } returns emptyList()

        val result = manageMovieDetailsUseCase.getMoviesByCategory(category.id, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCategory should throw when repository fails`() = runTest {
        val category = Genre(
            id = 1, name = "Action"
        )
        coEvery {
            movieRepository.getMoviesByCategory(
                category.id,
                1
            )
        } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getMoviesByCategory(category.id, 1)
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
        coEvery { movieRepository.getReviewsByMovieId(movieId, 1) } returns expected

        val result = manageMovieDetailsUseCase.getReviewsByMovieId(movieId, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getReviewsByMovieId should return empty list when none available`() = runTest {
        val movieId = 8
        coEvery { movieRepository.getReviewsByMovieId(movieId, 1) } returns emptyList()

        val result = manageMovieDetailsUseCase.getReviewsByMovieId(movieId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getReviewsByMovieId should throw when repository fails`() = runTest {
        val movieId = 9
        coEvery {
            movieRepository.getReviewsByMovieId(
                movieId,
                1
            )
        } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getReviewsByMovieId(movieId, 1)
        }
    }

    @Test
    fun `getSimilarMoviesByMovieId should return similar movies when available`() = runTest {
        val movieId = 10
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId, 1) } returns expected

        val result = manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSimilarMoviesByMovieId should return empty list when none available`() = runTest {
        val movieId = 11
        coEvery { movieRepository.getSimilarMoviesByMovieId(movieId, 1) } returns emptyList()

        val result = manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSimilarMoviesByMovieId should throw when repository fails`() = runTest {
        val movieId = 12
        coEvery {
            movieRepository.getSimilarMoviesByMovieId(
                movieId,
                1
            )
        } throws RetrievingDataFailureException(
            "Error"
        )

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getSimilarMoviesByMovieId(movieId, 1)
        }
    }

    @Test
    fun `getPopularMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getPopularMovies(1) } returns expected

        val result = manageMovieDetailsUseCase.getPopularMovies(1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getPopularMovies should throw when repository fails`() = runTest {
        coEvery { movieRepository.getPopularMovies(1) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getPopularMovies(1)
        }
    }

    @Test
    fun `getTopRatedMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getTopRatedMovies(1, dummyGenre.id) } returns expected

        val result = manageMovieDetailsUseCase.getTopRatedMovies(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTopRatedMovies should throw when repository fails`() = runTest {
        coEvery {
            movieRepository.getTopRatedMovies(
                1, dummyGenre.id
            )
        } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getTopRatedMovies(1, dummyGenre.id)
        }
    }

    @Test
    fun `getTrendingMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getTrendingMovies(1, dummyGenre.id) } returns expected

        val result = manageMovieDetailsUseCase.getTrendingMovies(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTrendingMovies should throw when repository fails`() = runTest {
        coEvery {
            movieRepository.getTrendingMovies(
                1, dummyGenre.id
            )
        } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getTrendingMovies(1, dummyGenre.id)
        }
    }

    @Test
    fun `getUpcomingMovies should return movies when available`() = runTest {
        val expected = listOf(mockk<Movie>())
        coEvery { movieRepository.getUpcomingMovies(1, genreId = dummyGenre.id) } returns expected

        val result = manageMovieDetailsUseCase.getUpcomingMovies(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `getUpcomingMovies should throw when repository fails`() = runTest {
        coEvery {
            movieRepository.getUpcomingMovies(
                1, dummyGenre.id
            )
        } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getUpcomingMovies(1, dummyGenre.id)
        }
    }

    @Test
    fun `getMovieGenres should return genres when available`() = runTest {
        val expected = listOf(dummyGenre)
        coEvery { movieRepository.getMovieGenres() } returns listOf(dummyGenre)

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

    @Test
    fun `getUserRatedMovies should return rated movies when available`() = runTest {
        val expected = listOf(mockk<Movie>(), mockk<Movie>())
        coEvery { movieRepository.getUserRatedMovies() } returns expected

        val result = manageMovieDetailsUseCase.getUserRatedMovies()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getUserRatedMovies should return empty list when none rated`() = runTest {
        coEvery { movieRepository.getUserRatedMovies() } returns emptyList()

        val result = manageMovieDetailsUseCase.getUserRatedMovies()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUserRatedMovies should throw exception when repository fails`() = runTest {
        coEvery { movieRepository.getUserRatedMovies() } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageMovieDetailsUseCase.getUserRatedMovies()
        }
    }

    @Test
    fun `deleteMovieRate should return true when deletion is successful`() = runTest {
        val movieId = 101
        coEvery { movieRepository.deleteMovieRate(movieId) } returns true

        val result = manageMovieDetailsUseCase.deleteMovieRate(movieId)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteMovieRate should return false when deletion fails`() = runTest {
        val movieId = 102
        coEvery { movieRepository.deleteMovieRate(movieId) } returns false

        val result = manageMovieDetailsUseCase.deleteMovieRate(movieId)

        assertThat(result).isFalse()
    }

    @Test
    fun `getMovieRate should return movie rating when available`() = runTest {
        val accountId = 1L
        val movieId = 101
        val expectedRating = 8

        coEvery { movieRepository.getMovieRate(accountId, movieId) } returns expectedRating

        val result = manageMovieDetailsUseCase.getMovieRate(accountId, movieId)

        assertThat(result).isEqualTo(expectedRating)
    }

    @Test
    fun `getMovieRate should return 0 when repository returns null`() = runTest {
        val accountId = 1L
        val movieId = 102

        coEvery { movieRepository.getMovieRate(accountId, movieId) } returns null

        val result = manageMovieDetailsUseCase.getMovieRate(accountId, movieId)

        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `addMovieRate should return true when rating is successful`() = runTest {
        val movieId = 201
        val rating = 7.5f

        coEvery { movieRepository.addMovieRate(movieId, rating) } returns true

        val result = manageMovieDetailsUseCase.addMovieRate(movieId, rating)

        assertThat(result).isTrue()
    }

    @Test
    fun `addMovieRate should return false when rating fails`() = runTest {
        val movieId = 202
        val rating = 5.0f

        coEvery { movieRepository.addMovieRate(movieId, rating) } returns false

        val result = manageMovieDetailsUseCase.addMovieRate(movieId, rating)

        assertThat(result).isFalse()
    }


    val dummyGenre = Genre(
        id = 1, name = "Action"
    )
}