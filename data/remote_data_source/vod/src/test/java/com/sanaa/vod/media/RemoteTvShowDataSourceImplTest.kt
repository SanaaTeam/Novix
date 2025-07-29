package com.sanaa.vod.media

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyEpisodeDto
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyGenreTvShowResponse
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyGuestOfHonor
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummySeasonDto
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowCastResponse
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowDto
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowGenresResponse
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowImagesResponse
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowReviewsResponse
import com.sanaa.vod.fakeData.TvShowDtoDummyData.dummyTvShowVideosResponse
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import com.sanaa.vod.media.tvShow.TvShowApiService
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
        coEvery { apiService.fetchTvShowsByCategory(1,1) } returns dummyGenreTvShowResponse

        val result = remoteTvShowDataSource.getTvShowsByGenre(1,1)

        assertThat(result.size == dummyGenreTvShowResponse.results.size)

    }

    @Test
    fun `getReviewsByTvShowId should return List of ReviewDto `() = runTest {
        coEvery { apiService.fetchTvShowsReviews(1,1) } returns dummyTvShowReviewsResponse

        val result = remoteTvShowDataSource.getReviewsByTvShowId(1,1)

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

        val result = remoteTvShowDataSource.getTvShowVideosUrls(1)

        assertThat(result.size == dummyTvShowVideosResponse.results.size)
    }

    @Test
    fun `getTvShowSeasonDetails should return SeasonDto `() = runTest {
        coEvery { apiService.fetchSeasonDetails(1, 1) } returns dummySeasonDto

        val result = remoteTvShowDataSource.getTvShowSeasonDetails(1, 1)

        assertThat(result.id == dummySeasonDto.id)
    }

    @Test
    fun `getTvShowGenres should return List of GenreDto `() = runTest {
        coEvery { apiService.fetchTvShowsGenres() } returns dummyTvShowGenresResponse

        val result = remoteTvShowDataSource.getTvShowGenres()

        assertThat(result.size == dummyTvShowGenresResponse.genres.size)
    }


}
