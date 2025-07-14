package usecase.details.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesSeasonsUseCase
import entity.Season
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvSeriesSeasonsUseCaseTest {

    private var repository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetTvSeriesSeasonsUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetTvSeriesSeasonsUseCase(repository)
    }

    @Test
    fun `execute() should return list of seasons from TvSeriesRepository`() = runTest {
        // Given
        val expected = listOf(mockk<Season>(), mockk<Season>())
        coEvery { repository.getTvSeriesSeason(1, 1) } returns expected

        // When
        val result = useCase.execute(1, 1)

        // Then
        coVerify { repository.getTvSeriesSeason(1, 1) }
        assert(result == expected)
    }
    @Test
    fun `execute() should throw exception when repository fails to get seasons`() = runTest {
        // Given
        val exception = IllegalStateException("Unable to fetch seasons")
        coEvery { repository.getTvSeriesSeason(1, 1) } throws exception

        // When, Then
        val result = assertThrows<IllegalStateException> {
            useCase.execute(1, 1)
        }

        assert(result.message == "Unable to fetch seasons")
    }
}