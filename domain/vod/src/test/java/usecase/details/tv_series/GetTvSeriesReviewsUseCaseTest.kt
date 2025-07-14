package usecase.details.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesReviewsUseCase
import entity.Review
import exceptions.NotFoundException
import extensions.now
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvSeriesReviewsUseCaseTest {
    private lateinit var getTvSeriesReviewsUseCase: GetTvSeriesReviewsUseCase
    private val repository: TvSeriesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getTvSeriesReviewsUseCase = GetTvSeriesReviewsUseCase(repository)
    }

    @Test
    fun `execute() should return tv series reviews when id is provided`() = runTest {
        //Given
        val id = 1
        coEvery { repository.getTvSeriesReviews(id) } returns listOf(dummyReview)
        //When
        val result = getTvSeriesReviewsUseCase.execute(id)
        // Then
        assert(result.isNotEmpty())
    }

    @Test
    fun `execute() should return an empty list when no tv series reviews are found for the given id`() =
        runTest {
            //Given
            val id = 1
            coEvery { repository.getTvSeriesReviews(id) } returns emptyList()
            //When
            val result = getTvSeriesReviewsUseCase.execute(id)
            // Then
            assert(result.isEmpty())
        }

    @Test
    fun `execute() should throw NotFoundException when no tv series reviews are found for the given id`() =
        runTest {
            //Given
            val id = 1
            coEvery { repository.getTvSeriesReviews(id) } throws NotFoundException("Message")

            // When & Then
            assertThrows<NotFoundException> { getTvSeriesReviewsUseCase.execute(id) }
        }

    val dummyReview = Review(
        id = 1,
        authorName = "Haider",
        userHandle = "itsHaider",
        avatarUrl = "Me",
        rating = 1.7f,
        content = "No content",
        createdDate = LocalDateTime.now().date
    )
}