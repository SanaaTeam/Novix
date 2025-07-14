package usecase.details.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetEpisodeImagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodeImagesUseCaseTest {

    private var repository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetEpisodeImagesUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetEpisodeImagesUseCase(repository)
    }

    @Test
    fun `execute() should return list of image URLs from TvSeriesRepository`() = runTest {
        // Given
        val expected = listOf("image1.jpg", "image2.jpg")
        coEvery { repository.getEpisodeImages(1, 1, 1) } returns expected

        // When
        val result = useCase.execute(1, 1, 1)

        // Then
        coVerify { repository.getEpisodeImages(1, 1, 1) }
        assert(result == expected)
    }
    @Test
    fun `execute() should throw exception when repository fails to get episode images`() = runTest {
        // Given
        val exception = IllegalStateException("Image loading failed")
        coEvery { repository.getEpisodeImages(1, 1, 1) } throws exception

        // When, Then
        val result = assertThrows<IllegalStateException> {
            useCase.execute(1, 1, 1)
        }

        assert(result.message == "Image loading failed")
    }
}