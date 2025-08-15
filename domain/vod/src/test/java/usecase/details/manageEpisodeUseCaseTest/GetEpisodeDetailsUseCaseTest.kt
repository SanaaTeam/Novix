package usecase.details.manageEpisodeUseCaseTest

import com.google.common.truth.Truth.assertThat
import entity.Episode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.manageEpisodeDetailsUseCase.GetEpisodeDetailsUseCase


class GetEpisodeDetailsUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(tvShowRepository)
    }

    @Test
    fun `getEpisodeDetails should return episode when available`() = runTest {
        val tvShowId = 1
        val season = 1
        val episode = 1
        val expected = mockk<Episode>()
        coEvery {
            tvShowRepository.getEpisodeDetails(
                tvShowId,
                season,
                episode
            )
        } returns expected

        val result = getEpisodeDetailsUseCase(tvShowId, season, episode)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeDetails should throw exception when repository fails`() = runTest {
        val tvShowId = 1
        val season = 1
        val episode = 1
        coEvery {
            tvShowRepository.getEpisodeDetails(
                tvShowId,
                season,
                episode
            )
        } throws IllegalStateException("Episode not found")

        assertThrows<IllegalStateException> {
            getEpisodeDetailsUseCase(tvShowId, season, episode)
        }
    }
}