package usecase.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesDetailsUseCase
import exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvSeriesDetailsUseCaseTest {
    private lateinit var getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase
    private val repository: TvSeriesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getTvSeriesDetailsUseCase = GetTvSeriesDetailsUseCase(repository)
    }

    @Test
    fun `execute() should return tv series details when id is provided`() = runTest {
        // Given
        val id = 1
        coEvery { repository.getTvSeriesDetails(id) } returns dummyTvSeries
        // When
        val result = getTvSeriesDetailsUseCase.execute(id)
        // Then
        assert(result == dummyTvSeries)

    }

    @Test
    fun `execute() should throw NotFoundException when no tv series details are found for the given id`() =
        runTest {
            // Given
            val id = 1
            coEvery { repository.getTvSeriesDetails(id) } throws NotFoundException("Message")

            // When & Then
            assertThrows<NotFoundException> {
                getTvSeriesDetailsUseCase.execute(id)
            }
        }
}