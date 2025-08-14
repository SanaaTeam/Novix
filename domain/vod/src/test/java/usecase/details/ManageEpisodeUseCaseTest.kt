package usecase.details

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Episode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.ManageEpisodeDetailsUseCase

class ManageEpisodeUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var manageEpisodeDetailsUseCase: ManageEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        manageEpisodeDetailsUseCase = ManageEpisodeDetailsUseCase(tvShowRepository)
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

        val result = manageEpisodeDetailsUseCase.getEpisodeDetails(tvShowId, season, episode)

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
            manageEpisodeDetailsUseCase.getEpisodeDetails(tvShowId, season, episode)
        }
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

        val result = manageEpisodeDetailsUseCase.getEpisodeGuestsOfHonor(tvShowId, season, episode)

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
            manageEpisodeDetailsUseCase.getEpisodeGuestsOfHonor(tvShowId, season, episode)
        }
    }

    @Test
    fun `getEpisodeImages should return images when available`() = runTest {
        val tvShowId = 3
        val season = 1
        val episode = 1
        val count = 5
        val expected = listOf("img1.jpg", "img2.jpg")
        coEvery {
            tvShowRepository.getEpisodeImageUrls(
                tvShowId,
                season,
                episode,
                count
            )
        } returns expected

        val result = manageEpisodeDetailsUseCase.getEpisodeImages(tvShowId, season, episode, count)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getEpisodeImages should throw exception when repository fails`() = runTest {
        val tvShowId = 3
        val season = 1
        val episode = 1
        val count = 5
        coEvery {
            tvShowRepository.getEpisodeImageUrls(
                tvShowId,
                season,
                episode,
                count
            )
        } throws IllegalStateException("Image loading failed")

        assertThrows<IllegalStateException> {
            manageEpisodeDetailsUseCase.getEpisodeImages(tvShowId, season, episode, count)
        }
    }
}