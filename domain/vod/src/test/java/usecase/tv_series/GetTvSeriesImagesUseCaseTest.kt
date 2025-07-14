package usecase.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesImagesUseCase
import exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvSeriesImagesUseCaseTest {
    private lateinit var getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase
    private val repository: TvSeriesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getTvSeriesImagesUseCase = GetTvSeriesImagesUseCase(repository)
    }

    @Test
    fun `execute() should return tv series images when id is provided`() = runTest {
        // Given
        val id = 1
        coEvery { repository.getTvSeriesImages(id) } returns listOf("Images")
        // When
        val result = getTvSeriesImagesUseCase.execute(id)
        // Then
        assert(result.isNotEmpty())
    }

    @Test
    fun `execute() should throw NotFoundException when no tv series images are found for the given id`() =
        runTest {
            // Given
            val id = 1
            coEvery { repository.getTvSeriesImages(id) } throws NotFoundException("Message")
            // When & Then
            assertThrows<NotFoundException> {
                getTvSeriesImagesUseCase.execute(id)
            }
        }

    @Test
    fun `execute() should return an empty list when no tv series images are found for the given id`() =
        runTest {
            // Given
            val id = 1
            coEvery { repository.getTvSeriesImages(id) } returns emptyList()
            // When
            val result = getTvSeriesImagesUseCase.execute(id)
            // Then
            assert(result.isEmpty())
        }
}