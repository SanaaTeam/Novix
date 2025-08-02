package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
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
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import repository.TvSeriesRepository
import kotlin.test.Test
import kotlin.test.assertEquals


class TvShowRepositoryImplTest {

    private lateinit var tvSeriesRepository: TvSeriesRepository
    private val preferences: PreferencesManager = mockk()
    private val remoteDataSource: RemoteTvShowDataSource = mockk(relaxed = true)


    @BeforeEach
    fun setup() {
        tvSeriesRepository = TvShowRepositoryImpl(remoteDataSource, preferences)
    }

    @Test
    fun `getTvShowDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowDetails(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { tvSeriesRepository.getTvSeriesDetails(999) }
    }

    @Test
    fun `getReviewsByTvShowId returns list`() = runTest {
        coEvery { remoteDataSource.getReviewsByTvShowId(1, 1) } returns listOf(dummyReviewDto)
        val result = tvSeriesRepository.getTvSeriesReviews(1, 1)
        assertEquals("A", result.first().authorName)
    }

    @Test
    fun `getReviewsByTvShowId throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getReviewsByTvShowId(any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            tvSeriesRepository.getTvSeriesReviews(
                999,
                1
            )
        }
    }

    @Test
    fun `getTvShowImageUrls throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowImageUrls(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            tvSeriesRepository.getTvSeriesImageUrls(
                99,
                1
            )
        }
    }

    @Test
    fun `getTvShowsByGenre throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowsByGenre(any(), 1) } throws Exception()
        assertThrows<RetrievingDataFailureException> { tvSeriesRepository.getTvSeriesByGenre(1, 1) }
    }

    @Test
    fun `getTvShowCast returns actors`() = runTest {
        coEvery { remoteDataSource.getTvShowCast(any()) } returns listOf(dummyActorDto)
        val result = tvSeriesRepository.getTvSeriesCast(1)
        assertEquals("Actor", result.first().name)
    }

    @Test
    fun `getTvShowCast throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowCast(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { tvSeriesRepository.getTvSeriesCast(999) }
    }

    @Test
    fun `getTvShowDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getTvShowDetails(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesDetails(1) }
    }

    @Test
    fun `getReviewsByTvShowId throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getReviewsByTvShowId(any(), 1) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesReviews(1, 1) }
    }


    @Test
    fun `getTvShowImageUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getTvShowImageUrls(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesImageUrls(1, 3) }
    }


    @Test
    fun `getTvShowsByGenre throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getTvShowsByGenre(any(), 1) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesByGenre(1, 1) }
    }

    @Test
    fun `getTvShowCast throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getTvShowCast(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesCast(1) }
    }

    @Test
    fun `getTvShowSeasonDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery {
            remoteDataSource.getTvShowSeasonDetails(
                any(),
                any()
            )
        } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesSeason(1, 1) }
    }

    @Test
    fun `getEpisodeDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery {
            remoteDataSource.getEpisodeDetails(
                any(),
                any(),
                any()
            )
        } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getEpisodeDetails(1, 1, 1) }
    }


    @Test
    fun `getEpisodeImageUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery {
            remoteDataSource.getEpisodeImageUrls(
                any(),
                any(),
                any()
            )
        } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getEpisodeImageUrls(1, 1, 1, 1) }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws NoNetworkException on ConnectionException`() = runTest {
        coEvery {
            remoteDataSource.getEpisodeGuestsOfHonor(
                any(), any(), any()
            )
        } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getEpisodeGuestsOfHonor(1, 1, 1) }
    }


    @Test
    fun `getTvShowVideosUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteDataSource.getTvShowVideosUrls(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { tvSeriesRepository.getTvSeriesTrailer(1) }
    }


    @Test
    fun `getTvShowSeasonDetails returns season entity`() = runTest {
        coEvery { remoteDataSource.getTvShowSeasonDetails(any(), any()) } returns dummySeasonDto
        val result = tvSeriesRepository.getTvSeriesSeason(1, 1)
        assertEquals("Season 1", result.title)
    }

    @Test
    fun `getTvShowSeasonDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowSeasonDetails(any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { tvSeriesRepository.getTvSeriesSeason(1, 99) }
    }

    // --- Episode ---
    @Test
    fun `getEpisodeDetails returns episode entity`() = runTest {
        coEvery { remoteDataSource.getEpisodeDetails(any(), any(), any()) } returns dummyEpisodeDto
        val result = tvSeriesRepository.getEpisodeDetails(1, 1, 1)
        assertEquals("Episode", result.title)
    }

    @Test
    fun `getEpisodeDetails throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getEpisodeDetails(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            tvSeriesRepository.getEpisodeDetails(
                1,
                1,
                999
            )
        }
    }

    @Test
    fun `getEpisodeImageUrls returns list`() = runTest {
        coEvery {
            remoteDataSource.getEpisodeImageUrls(
                any(),
                any(),
                any()
            )
        } returns listOf(ImageDto("2"))
        val result = tvSeriesRepository.getEpisodeImageUrls(1, 1, 1, 1)
        assertEquals(1, result.size)
    }

    @Test
    fun `getEpisodeImageUrls throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getEpisodeImageUrls(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            tvSeriesRepository.getEpisodeImageUrls(
                1,
                1,
                99,
                11
            )
        }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getEpisodeGuestsOfHonor(any(), any(), any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> {
            tvSeriesRepository.getEpisodeGuestsOfHonor(
                1, 1, 99
            )
        }
    }

    @Test
    fun `getTvShowVideosUrls returns null if no videos`() = runTest {
        coEvery { remoteDataSource.getTvShowVideosUrls(any()) } returns emptyList()
        val result = tvSeriesRepository.getTvSeriesTrailer(1)
        assertNull(result)
    }

    @Test
    fun `getTvShowVideosUrls throws RetrievingDataFailureException`() = runTest {
        coEvery { remoteDataSource.getTvShowVideosUrls(any()) } throws Exception()
        assertThrows<RetrievingDataFailureException> { tvSeriesRepository.getTvSeriesTrailer(404) }
    }

    @Test
    fun `getTvShowVideosUrls return url if there is a video`() = runTest {
        coEvery { remoteDataSource.getTvShowVideosUrls(any()) } returns listOf(
            VideoDto(
                id = "1", key = "xyz", site = "YouTube", type = "Trailer"
            )
        )
        val result = tvSeriesRepository.getTvSeriesTrailer(1)
        assertNotNull(result)

    }

    @Test
    fun `getTopRatedTvSeries returns empty list`() = runTest {
        // Act
        val result = tvSeriesRepository.getTopRatedTvSeries(page = 1, genreId = 1)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getTrendingTvSeries returns empty list`() = runTest {
        // Act
        val result = tvSeriesRepository.getTrendingTvSeries(1, null)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getPopularSeries returns empty list`() = runTest {
        // Act
        val result = tvSeriesRepository.getPopularSeries(page = 1)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getSeriesGenres returns list of Genre`() = runTest {
        coEvery { remoteDataSource.getTvShowGenres() } returns dummyGenresDto

        val result = tvSeriesRepository.getSeriesGenres()

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getSeriesRate should return empty list when there is no series rate returned from server`() =
        runTest {
            // Given
            val accountId = 1L
            val sessionId = "345678"
            coEvery { preferences.sessionId } returns flowOf(sessionId)
            coEvery { remoteDataSource.getTvShowRate(accountId, sessionId) } returns emptyList()

            // When
            val result = tvSeriesRepository.getSeriesRate(accountId)

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getEpisodesRate should return empty list when there is no episodes rate returned from server`() =
        runTest {
            // Given
            val accountId = 1L
            val sessionId = "345678"
            coEvery { preferences.sessionId } returns flowOf(sessionId)
            coEvery { remoteDataSource.getEpisodesRate(accountId, sessionId) } returns emptyList()

            // When
            val result = tvSeriesRepository.getEpisodesRate(accountId)

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `addTvSeriesRate should call RemoteTvShowDataSource when try to add tv series rate`() =
        runTest {
            val sessionId = "32453"
            val seriesId = 1
            val rating = 1f
            coEvery { preferences.sessionId } returns flowOf(sessionId)

            tvSeriesRepository.addTvSeriesRate(seriesId, rating)

            coVerify { remoteDataSource.sendTvSeriesRate(seriesId, sessionId, rating) }
        }

    @Test
    fun `addTvEpisodeRate should call RemoteTvShowDataSource when try to add tv episode rate`() =
        runTest {
            val seriesId = 1
            val seasonNumber = 1
            val episodeNumber = 1
            val sessionId = "32453"
            val rating = 1f
            coEvery { preferences.sessionId } returns flowOf(sessionId)

            tvSeriesRepository.addTvEpisodeRate(
                seriesId,
                seasonNumber,
                episodeNumber,
                rating
            )

            coVerify {
                remoteDataSource.sendTvEpisodeRate(
                    seriesId,
                    seasonNumber,
                    episodeNumber,
                    sessionId,
                    rating
                )
            }
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