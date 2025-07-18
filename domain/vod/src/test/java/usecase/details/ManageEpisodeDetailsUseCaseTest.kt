package usecase.details

import com.google.common.truth.Truth.assertThat
import details.repository.TvSeriesRepository
import details.usecase.ManageEpisodeDetailsUseCase
import entity.Actor
import entity.Episode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ManageEpisodeDetailsUseCaseTest {

    private val tvSeriesRepository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var manageEpisodeDetailsUseCase: ManageEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        manageEpisodeDetailsUseCase = ManageEpisodeDetailsUseCase(tvSeriesRepository)
    }

    @Test
    fun `getEpisodeDetails should return episode when available`() = runTest {
        val seriesId = 1
        val season = 1
        val episode = 1
        val expected = mockk<Episode>()
        coEvery {
            tvSeriesRepository.getEpisodeDetails(
                seriesId,
                season,
                episode
            )
        } returns expected

        val result = manageEpisodeDetailsUseCase.getEpisodeDetails(seriesId, season, episode)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeDetails should throw exception when repository fails`() = runTest {
        val seriesId = 1
        val season = 1
        val episode = 1
        coEvery {
            tvSeriesRepository.getEpisodeDetails(
                seriesId,
                season,
                episode
            )
        } throws IllegalStateException("Episode not found")

        assertThrows<IllegalStateException> {
            manageEpisodeDetailsUseCase.getEpisodeDetails(seriesId, season, episode)
        }
    }

    @Test
    fun `getEpisodeGuestsOfHonor should return guests when available`() = runTest {
        val seriesId = 2
        val season = 1
        val episode = 1
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery {
            tvSeriesRepository.getEpisodeGuestsOfHonor(
                seriesId,
                season,
                episode
            )
        } returns expected

        val result = manageEpisodeDetailsUseCase.getEpisodeGuestsOfHonor(seriesId, season, episode)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeGuestsOfHonor should throw exception when repository fails`() = runTest {
        val seriesId = 2
        val season = 1
        val episode = 1
        coEvery {
            tvSeriesRepository.getEpisodeGuestsOfHonor(
                seriesId,
                season,
                episode
            )
        } throws IllegalStateException("Guests not available")

        assertThrows<IllegalStateException> {
            manageEpisodeDetailsUseCase.getEpisodeGuestsOfHonor(seriesId, season, episode)
        }
    }

    @Test
    fun `getEpisodeImages should return images when available`() = runTest {
        val seriesId = 3
        val season = 1
        val episode = 1
        val count = 5
        val expected = listOf("img1.jpg", "img2.jpg")
        coEvery {
            tvSeriesRepository.getEpisodeImages(
                seriesId,
                season,
                episode,
                count
            )
        } returns expected

        val result = manageEpisodeDetailsUseCase.getEpisodeImages(seriesId, season, episode, count)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeImages should throw exception when repository fails`() = runTest {
        val seriesId = 3
        val season = 1
        val episode = 1
        val count = 5
        coEvery {
            tvSeriesRepository.getEpisodeImages(
                seriesId,
                season,
                episode,
                count
            )
        } throws IllegalStateException("Image loading failed")

        assertThrows<IllegalStateException> {
            manageEpisodeDetailsUseCase.getEpisodeImages(seriesId, season, episode, count)
        }
    }
}