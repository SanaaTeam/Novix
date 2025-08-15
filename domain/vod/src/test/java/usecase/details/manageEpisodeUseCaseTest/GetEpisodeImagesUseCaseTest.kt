package usecase.details.manageEpisodeUseCaseTest

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.manageEpisodeDetailsUseCase.GetEpisodeImagesUseCase


class GetEpisodeImagesUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var getEpisodeImagesUseCase: GetEpisodeImagesUseCase

    @BeforeEach
    fun setUp() {
        getEpisodeImagesUseCase = GetEpisodeImagesUseCase(tvShowRepository)
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

        val result = getEpisodeImagesUseCase(tvShowId, season, episode, count)

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
            getEpisodeImagesUseCase(tvShowId, season, episode, count)
        }
    }
}