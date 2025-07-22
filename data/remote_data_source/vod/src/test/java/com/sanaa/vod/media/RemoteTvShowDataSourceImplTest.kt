package com.sanaa.vod.media

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import com.sanaa.vod.media.tvShow.TvShowApiService
import com.sanaa.vod.media.tvShow.response.GenreTvShowResponse
import com.sanaa.vod.media.tvShow.response.TvShowCastResponse
import com.sanaa.vod.media.tvShow.response.TvShowGuestOfStarsResponse
import com.sanaa.vod.media.tvShow.response.TvShowImagesResponse
import com.sanaa.vod.media.tvShow.response.TvShowReviewsResponse
import com.sanaa.vod.media.tvShow.response.TvShowVideosResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteTvShowDataSourceImplTest {
    lateinit var remoteTvShowDataSource: RemoteTvShowDataSource
    private val apiService: TvShowApiService = mockk()

    @BeforeEach
    fun setUp() {
        remoteTvShowDataSource = RemoteTvShowDataSourceImpl(apiService)
    }

    @Test
    fun `getTvShowDetails should return TvShowDto `() = runTest {
        coEvery { apiService.fetchTvShowsDetails(1) } returns dummyTvShowDto

        val result = remoteTvShowDataSource.getTvShowDetails(1)

        assertThat(result.id == dummyTvShowDto.id)

    }

    @Test
    fun `getTvShowImageUrls should return List of ImageDto `() = runTest {
        coEvery { apiService.fetchTvShowsImages(1) } returns dummyTvShowImagesResponse

        val result = remoteTvShowDataSource.getTvShowImageUrls(1)

        assertThat(result.size == dummyTvShowImagesResponse.backdrops.size)
    }

    @Test
    fun `getTvShowsByGenre should return List of TvShowDto `() = runTest {
        coEvery { apiService.fetchTvShowsByCategory(1) } returns dummyGenreTvShowResponse

        val result = remoteTvShowDataSource.getTvShowsByGenre(1)

        assertThat(result.size == dummyGenreTvShowResponse.results.size)

    }

    @Test
    fun `getReviewsByTvShowId should return List of ReviewDto `() = runTest {
        coEvery { apiService.fetchTvShowsReviews(1) } returns dummyTvShowReviewsResponse

        val result = remoteTvShowDataSource.getReviewsByTvShowId(1)

        assertThat(result.size == dummyTvShowReviewsResponse.results.size)

    }

    @Test
    fun `getTvShowCast should return List of ActorDto `() = runTest {
        coEvery { apiService.fetchTvShowsCast(1) } returns dummyTvShowCastResponse

        val result = remoteTvShowDataSource.getTvShowCast(1)

        assertThat(result.size == dummyTvShowCastResponse.cast.size)
    }

    @Test
    fun `getEpisodeDetails should return EpisodeDto `() = runTest {
        coEvery { apiService.fetchEpisodeDetails(1, 1, 1) } returns dummyEpisodeDto

        val result = remoteTvShowDataSource.getEpisodeDetails(1, 1, 1)

        assertThat(result.id == dummyEpisodeDto.id)

    }

    @Test
    fun `getEpisodeImageUrls should return List of ImageDto `() = runTest {
        coEvery { apiService.fetchEpisodeImages(1, 1, 1) } returns dummyTvShowImagesResponse

        val result = remoteTvShowDataSource.getEpisodeImageUrls(1, 1, 1)

        assertThat(result.size == dummyTvShowImagesResponse.backdrops.size)

    }

    @Test
    fun `getEpisodeGuestsOfHonor should return List of ActorDto `() = runTest {
        coEvery { apiService.fetchEpisodeGuestsOfHonor(1, 1, 1) } returns dummyGuestOfHonor

        val result = remoteTvShowDataSource.getEpisodeGuestsOfHonor(1, 1, 1)

        assertThat(result.size == dummyGuestOfHonor.guestStars.size)
    }

    @Test
    fun `getTvShowVideos should return List of VideoDto `() = runTest {
        coEvery { apiService.fetchTvShowsVideos(1) } returns dummyTvShowVideosResponse

        val result = remoteTvShowDataSource.getTvShowVideos(1)

        assertThat(result.size == dummyTvShowVideosResponse.results.size)
    }

    @Test
    fun `getTvShowSeasonDetails should return SeasonDto `() = runTest {
        coEvery { apiService.fetchSeasonDetails(1, 1) } returns dummySeasonDto

        val result = remoteTvShowDataSource.getTvShowSeasonDetails(1, 1)

        assertThat(result.id == dummySeasonDto.id)
    }


    companion object {
        val dummyTvShowDto = TvShowDto(
            id = 1,
            name = "name",
            overview = "overview",
            posterPath = "posterPath",
            voteAverage = 1f,
        )
        val dummyTvShowImagesResponse = TvShowImagesResponse(
            backdrops = listOf(
                ImageDto(
                    filePath = "filePath",
                )
            )
        )
        val dummyGenreTvShowResponse = GenreTvShowResponse(
            results = listOf(
                TvShowDto(
                    id = 1,
                    name = "name",
                    overview = "overview",
                )
            ), page = 1
        )
        val dummyTvShowReviewsResponse = TvShowReviewsResponse(
            results = listOf(
                ReviewDto(
                    id = "1",
                    author = "author",
                    content = "content",
                    createdAt = "createdAt",
                )

            ), id = 1, page = 1
        )
        val dummyTvShowCastResponse = TvShowCastResponse(
            cast = listOf(
                ActorDto(
                    id = 1,
                    name = "name",
                )
            )
        )
        val dummyEpisodeDto = EpisodeDto(
            id = 1,
            name = "name",
        )
        val dummyGuestOfHonor = TvShowGuestOfStarsResponse(
            guestStars = listOf(
                ActorDto(
                    id = 1,
                    name = "name",
                )
            )
        )
        val dummyTvShowVideosResponse = TvShowVideosResponse(
            id = 1, results = listOf(
                VideoDto(
                    id = "1",
                    key = "key",
                    name = "name",
                )
            )
        )

        val dummySeasonDto = SeasonDto(
            id = 1,
            name = "name",
        )
    }
}
