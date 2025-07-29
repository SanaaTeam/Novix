package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import repository.TvSeriesRepository
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
    fun `getTvShowDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesDetails(999) }
    }

    @Test
    fun `getReviewsByTvShowId returns list`() = runTest {
        coEvery { remote.getReviewsByTvShowId(1,1) } returns listOf(dummyReviewDto)
        val result = repository.getTvSeriesReviews(1,1)
        assertEquals("A", result.first().authorName)
    }

    @Test
    fun `getReviewsByTvShowId throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any(),any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesReviews(999,1) }
    }

    @Test
    fun `getTvShowImageUrls throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesImageUrls(99, 1) }
    }

    @Test
    fun `getTvShowsByGenre throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any(),1) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesByGenre(1,1) }
    }

    @Test
    fun `getTvShowCast returns actors`() = runTest {
        coEvery { remote.getTvShowCast(any()) } returns listOf(dummyActorDto)
        val result = repository.getTvSeriesCast(1)
        assertEquals("Actor", result.first().name)
    }

    @Test
    fun `getTvShowCast throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesCast(999) }
    }

    @Test
    fun `getTvShowDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesDetails(1) }
    }

    @Test
    fun `getReviewsByTvShowId throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any(),1) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesReviews(1,1) }
    }


    @Test
    fun `getTvShowImageUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesImageUrls(1, 3) }
    }


    @Test
    fun `getTvShowsByGenre throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any(),1) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesByGenre(1,1) }
    }

    @Test
    fun `getTvShowCast throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesCast(1) }
    }

    @Test
    fun `getTvShowSeasonDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesSeason(1, 1) }
    }

    @Test
    fun `getEpisodeDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getEpisodeDetails(1, 1, 1) }
    }


    @Test
    fun `getEpisodeImageUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getEpisodeImageUrls(any(), any(), any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getEpisodeImageUrls(1, 1, 1, 1) }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws NoNetworkException on ConnectionException`() = runTest {
        coEvery {
            remote.getEpisodeGuestsOfHonor(
                any(), any(), any()
            )
        } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getEpisodeGuestsOfHonor(1, 1, 1) }
    }


    @Test
    fun `getTvShowVideosUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvSeriesTrailer(1) }
    }


    @Test
    fun `getTvShowSeasonDetails returns season entity`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } returns dummySeasonDto
        val result = repository.getTvSeriesSeason(1, 1)
        assertEquals("Season 1", result.title)
    }

    @Test
    fun `getTvShowSeasonDetails throws RetrievingDataFailureException`() = runTest {
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
    fun `getEpisodeImageUrls returns list`() = runTest {
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
    fun `getEpisodeImageUrls throws RetrievingDataFailureException`() = runTest {
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
    fun `getTvShowVideosUrls returns null if no videos`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } returns emptyList()
        val result = repository.getTvSeriesTrailer(1)
        assertNull(result)
    }

    @Test
    fun `getTvShowVideosUrls throws RetrievingDataFailureException`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { repository.getTvSeriesTrailer(404) }
    }

    @Test
    fun `getTvShowVideosUrls return url if there is a video`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } returns listOf(
            VideoDto(
                id = "1", key = "xyz", site = "YouTube", type = "Trailer"
            )
        )
        val result = repository.getTvSeriesTrailer(1)
        assertNotNull(result)

    }

    @Test
    fun `getTopRatedTvSeries returns empty list`() = runTest {
        // Act
        val result = repository.getTopRatedTvSeries(page = 1, genreId = 1)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getTrendingTvSeries returns empty list`() = runTest {
        // Act
        val result = repository.getTrendingTvSeries(1, null)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getPopularSeries returns empty list`() = runTest {
        // Act
        val result = repository.getPopularSeries(page = 1)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getSeriesGenres returns list of Genre`() = runTest {
        coEvery { remote.getTvShowGenres() } returns dummyGenresDto

        val result = repository.getSeriesGenres()

        assertThat(result).isNotEmpty()
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

    val dummyGenresDto = listOf(
        GenreDto(id = 1, name = "Action"),
        GenreDto(id = 2, name = "Drama")
    )
}