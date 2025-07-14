package usecase.details.movie

import com.google.common.truth.Truth.assertThat
import details.repository.MovieRepository
import details.usecase.movie.GetReviewsByMovieId
import entity.Review
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetReviewsByMovieIdTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getReviewsByMovieId: GetReviewsByMovieId

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getReviewsByMovieId = GetReviewsByMovieId(movieRepository)
    }
    @Test
    fun `execute() should return reviews when available`() = runTest {
        // Given
        val movieId = 1
        val reviews = listOf(testReview1, testReview2)
        coEvery { movieRepository.getReviewsByMovieId(movieId) } returns reviews

        // When
        val result = getReviewsByMovieId.execute(movieId)

        // Then
        assertThat(result).isEqualTo(reviews)
    }

    @Test
    fun `execute() should return empty list when there are no reviews`() = runTest {
        // Given
        val movieId = 1
        coEvery { movieRepository.getReviewsByMovieId(movieId) } returns emptyList()

        // When
        val result = getReviewsByMovieId.execute(movieId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `execute() should throw exception when repository fails`() = runTest {
        // Given
        val movieId = 1
        coEvery {
            movieRepository.getReviewsByMovieId(movieId)
        } throws RetrievingDataFailureException("Failed to retrieve reviews")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getReviewsByMovieId.execute(movieId)
        }
    }
    companion object {
        private val testReview1 = Review(
            id = 1,
            authorName = "John Doe",
            userHandle = "@johndoe",
            avatarUrl = "https://example.com/avatar1.png",
            rating = 4.5f,
            content = "Great movie, loved it!",
            createdDate = LocalDate(2023, 6, 12)
        )

        private val testReview2 = Review(
            id = 2,
            authorName = "Jane Smith",
            userHandle = null,
            avatarUrl = null,
            rating = null,
            content = "It was okay, not the best.",
            createdDate = LocalDate(2023, 6, 14)
        )
    }
}