package usecase.details.manageEpisodeUseCaseTest

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.manageEpisodeDetailsUseCase.AddTvEpisodeRateUseCase


class AddTvEpisodeRateUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var addTvEpisodeRateUseCase: AddTvEpisodeRateUseCase

    @BeforeEach
    fun setUp() {
        addTvEpisodeRateUseCase = AddTvEpisodeRateUseCase(tvShowRepository)
    }

    @Test
    fun `addTvEpisodeRate should return true on success`() = runTest {
        val tvShowId = 1
        val season = 1
        val episode = 1
        val rating = 8.5f
        coEvery {
            tvShowRepository.addTvEpisodeRate(
                tvShowId,
                season,
                episode,
                rating
            )
        } returns true

        val result = addTvEpisodeRateUseCase(tvShowId, season, episode, rating)

        assertThat(result).isTrue()
    }

    @Test
    fun `addTvEpisodeRate should return false on failure`() = runTest {
        val tvShowId = 1
        val season = 1
        val episode = 1
        val rating = 8.5f
        coEvery {
            tvShowRepository.addTvEpisodeRate(
                tvShowId,
                season,
                episode,
                rating
            )
        } returns false

        val result = addTvEpisodeRateUseCase(tvShowId, season, episode, rating)

        assertThat(result).isFalse()
    }

    @Test
    fun `addTvEpisodeRate should throw exception when repository fails`() = runTest {
        val tvShowId = 1
        val season = 1
        val episode = 1
        val rating = 8.5f
        coEvery {
            tvShowRepository.addTvEpisodeRate(
                tvShowId,
                season,
                episode,
                rating
            )
        } throws IllegalStateException("Failed to add rate")

        assertThrows<IllegalStateException> {
            addTvEpisodeRateUseCase(tvShowId, season, episode, rating)
        }
    }
}