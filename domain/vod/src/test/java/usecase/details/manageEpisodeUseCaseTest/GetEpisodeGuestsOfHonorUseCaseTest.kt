package usecase.details.manageEpisodeUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Actor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.manageEpisodeDetailsUseCase.GetEpisodeGuestsOfHonorUseCase


class GetEpisodeGuestsOfHonorUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var getEpisodeGuestsOfHonorUseCase: GetEpisodeGuestsOfHonorUseCase

    @BeforeEach
    fun setUp() {
        getEpisodeGuestsOfHonorUseCase = GetEpisodeGuestsOfHonorUseCase(tvShowRepository)
    }

    @Test
    fun `getEpisodeGuestsOfHonor should return guests when available`() = runTest {
        val tvShowId = 2
        val season = 1
        val episode = 1
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery {
            tvShowRepository.getEpisodeGuestsOfHonor(
                tvShowId,
                season,
                episode
            )
        } returns expected

        val result = getEpisodeGuestsOfHonorUseCase(tvShowId, season, episode)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeGuestsOfHonor should throw exception when repository fails`() = runTest {
        val tvShowId = 2
        val season = 1
        val episode = 1
        coEvery {
            tvShowRepository.getEpisodeGuestsOfHonor(
                tvShowId,
                season,
                episode
            )
        } throws IllegalStateException("Guests not available")

        assertThrows<IllegalStateException> {
            getEpisodeGuestsOfHonorUseCase(tvShowId, season, episode)
        }
    }
}