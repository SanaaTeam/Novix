package usecase

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetEpisodeGuestsOfHonorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetEpisodeGuestsOfHonorUseCaseTest {

    private var repository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetEpisodeGuestsOfHonorUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetEpisodeGuestsOfHonorUseCase(repository)
    }

    @Test
    fun `execute() should return list of guests from TvSeriesRepository`() = runTest {
        // Given
        val expected = listOf(mockk<entity.Actor>(), mockk<entity.Actor>())
        coEvery { repository.getEpisodeGuestsOfHonor(1, 1, 1) } returns expected

        // When
        val result = useCase.execute(1, 1, 1)

        // Then
        coVerify { repository.getEpisodeGuestsOfHonor(1, 1, 1) }
        assert(result == expected)
    }
    @Test
    fun `execute() should throw exception when repository fails to get guests of honor`() = runTest {
        // Given
        val exception = IllegalStateException("Guests not available")
        coEvery { repository.getEpisodeGuestsOfHonor(1, 1, 1) } throws exception

        // When, Then
        val result = assertThrows<IllegalStateException> {
            useCase.execute(1, 1, 1)
        }

        assert(result.message == "Guests not available")
    }
}