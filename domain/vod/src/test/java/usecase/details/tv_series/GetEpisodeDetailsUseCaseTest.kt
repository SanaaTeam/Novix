package usecase.details.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetEpisodeDetailsUseCase
import entity.Episode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


class GetEpisodeDetailsUseCaseTest {

    private var repository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetEpisodeDetailsUseCase(repository)
    }

    @Test
    fun `execute() should return episode details from TvSeriesRepository`() = runTest {
        // Given
        val expected = mockk<Episode>()
        coEvery { repository.getEpisodeDetails(1, 1, 1) } returns expected

        // When
        val result = useCase.execute(1, 1, 1)

        // Then
        coVerify { repository.getEpisodeDetails(1, 1, 1) }
        assert(result == expected)
    }
    @Test
    fun `execute() should throw exception when repository fails to get episode details`() = runTest {
        // Given
        val exception = IllegalStateException("Episode not found")
        coEvery { repository.getEpisodeDetails(1, 1, 1) } throws exception

        // When, Then
        val result = assertThrows<IllegalStateException> {
            useCase.execute(1, 1, 1)
        }

        assert(result.message == "Episode not found")
    }
}