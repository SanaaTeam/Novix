package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import details.repository.TvSeriesRepository
import entity.Genre
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException
import kotlin.test.Test
import kotlin.test.assertEquals


class TvShowRepositoryImplTest {

    private lateinit var repository: TvSeriesRepository
    private val remote: RemoteTvShowDataSource = mockk(relaxed = true)


    @BeforeEach
    fun setup() {
        repository = TvShowRepositoryImpl(remote)
    }

    @Test
    fun `getTvSeriesDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesDetails(999) }
    }

    @Test
    fun `getTvSeriesReviews returns list`() = runTest {
        coEvery { remote.getReviewsByTvShowId(1) } returns listOf(dummyReviewDto)
        val result = repository.getTvSeriesReviews(1)
        assertEquals("A", result.first().authorName)
    }

    @Test
    fun `getTvSeriesReviews throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesReviews(999) }
    }

    @Test
    fun `getTvSeriesImages throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesImageUrls(99, 1) }
    }

    @Test
    fun `getTvSeriesByGenre throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesByGenre(Genre.CRIME) }
    }

    @Test
    fun `getTvSeriesCast returns actors`() = runTest {
        coEvery { remote.getTvShowCast(any()) } returns listOf(dummyActorDto)
        val result = repository.getTvSeriesCast(1)
        assertEquals("Actor", result.first().name)
    }

    @Test
    fun `getTvSeriesCast throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesCast(999) }
    }

    @Test
    fun `getTvSeriesDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesDetails(1) }
    }

    @Test
    fun `getTvSeriesReviews throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesReviews(1) }
    }


    @Test
    fun `getTvSeriesImages throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesImageUrls(1, 3) }
    }


    @Test
    fun `getTvSeriesByGenre throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesByGenre(Genre.CRIME) }
    }

    @Test
    fun `getTvSeriesCast throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesCast(1) }
    }

    @Test
    fun `getTvSeriesSeason throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesSeason(1, 1) }
    }

    @Test
    fun `getEpisodeDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getEpisodeDetails(1, 1, 1) }
    }


    @Test
    fun `getEpisodeImages throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getEpisodeImageUrls(any(), any(), any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getEpisodeImageUrls(1, 1, 1, 1) }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery {
            remote.getEpisodeGuestsOfHonor(
                any(), any(), any()
            )
        } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getEpisodeGuestsOfHonor(1, 1, 1) }
    }


    @Test
    fun `getTvSeriesTrailer throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.getTvShowVideos(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> { repository.getTvSeriesTrailer(1) }
    }


    @Test
    fun `getTvSeriesSeason returns season entity`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } returns dummySeasonDto
        val result = repository.getTvSeriesSeason(1, 1)
        assertEquals("Season 1", result.title)
    }

    @Test
    fun `getTvSeriesSeason throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesSeason(1, 99) }
    }

    // --- Episode ---
    @Test
    fun `getEpisodeDetails returns episode entity`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } returns dummyEpisodeDto
        val result = repository.getEpisodeDetails(1, 1, 1)
        assertEquals("Episode", result.title)
    }

    @Test
    fun `getEpisodeDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getEpisodeDetails(1, 1, 999) }
    }

    @Test
    fun `getEpisodeImages returns list`() = runTest {
        coEvery {
            remote.getEpisodeImageUrls(
                any(),
                any(),
                any()
            )
        } returns listOf(ImageDto("2"))
        val result = repository.getEpisodeImageUrls(1, 1, 1, 1)
        assertEquals(1, result.size)
    }

    @Test
    fun `getEpisodeImages throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getEpisodeImageUrls(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            repository.getEpisodeImageUrls(
                1,
                1,
                99,
                11
            )
        }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getEpisodeGuestsOfHonor(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            repository.getEpisodeGuestsOfHonor(
                1, 1, 99
            )
        }
    }

    @Test
    fun `getTvSeriesTrailer returns null if no videos`() = runTest {
        coEvery { remote.getTvShowVideos(any()) } returns emptyList()
        val result = repository.getTvSeriesTrailer(1)
        assertNull(result)
    }

    @Test
    fun `getTvSeriesTrailer throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowVideos(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesTrailer(404) }
    }

    @Test
    fun `getTvSeriesTrailer return url if there is a video`() = runTest {
        coEvery { remote.getTvShowVideos(any()) } returns listOf(
            VideoDto(
                id = "1", key = "xyz", site = "YouTube", type = "Trailer"
            )
        )
        val result = repository.getTvSeriesTrailer(1)
        assertNotNull(result)

    }

    private val dummyReviewDto = ReviewDto(
        id = "1", content = "Review", authorDetails = AuthorDetailsDto(
            name = "A", username = "a", avatarPath = "/avatar.png", rating = 4.0f
        ), createdAt = "2024-01-01"
    )

    private val dummyActorDto = ActorDto(
        id = 10, name = "Actor", gender = 2,
    )

    private val dummySeasonDto = SeasonDto(
        id = 5, name = "Season 1", overview = "intro", seasonNumber = 1, episodes = emptyList()
    )

    private val dummyEpisodeDto = EpisodeDto(
        id = 8,
        name = "Episode",
        overview = "desc",
        seasonNumber = 1,
        episodeNumber = 1,
        voteAverage = 8f,
        airDate = "2024-01-01",
        runtime = 50
    )
}