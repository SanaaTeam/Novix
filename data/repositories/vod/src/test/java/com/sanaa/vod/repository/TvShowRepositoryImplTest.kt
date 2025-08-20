package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.dataSource.remote.RemoteTvShowDataSource
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.dataSource.remote.dto.review.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.SeasonDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import com.sanaa.vod.repository.mapper.cachedContent.toEntity
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class TvShowRepositoryImplTest {

    private lateinit var repository: TvShowRepository
    private val preferences: PreferencesManager = mockk()
    private val remote: RemoteTvShowDataSource = mockk(relaxed = true)
    private val local: LocalCachedContentDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        repository = TvShowRepositoryImpl(remote, local, preferences)
    }

    @Test
    fun `getTvShowDetails throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowDetails(999) }
    }

    @Test
    fun `getReviewsByTvShowId returns list`() = runTest {
        coEvery { remote.getReviewsByTvShowId(1, 1) } returns listOf(dummyReviewDto)
        val result = repository.getTvShowReviews(1, 1)
        assertEquals("A", result.first().authorName)
    }

    @Test
    fun `getReviewsByTvShowId throws NovixAppException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any(), any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowReviews(999, 1) }
    }

    @Test
    fun `getTvShowImageUrls throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowImageUrls(99, 1) }
    }

    @Test
    fun `getTvShowsByGenre throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any(), 1) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowsByGenre(1, 1) }
    }

    @Test
    fun `getTvShowCast returns actors`() = runTest {
        coEvery { remote.getTvShowCast(any()) } returns listOf(dummyActorDto)
        val result = repository.getTvShowCast(1)
        assertEquals("Actor", result.first().name)
    }

    @Test
    fun `getTvShowCast throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowCast(999) }
    }

    @Test
    fun `getTvShowDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowDetails(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowDetails(1) }
    }

    @Test
    fun `getReviewsByTvShowId throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getReviewsByTvShowId(any(), 1) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowReviews(1, 1) }
    }

    @Test
    fun `getTvShowImageUrls throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowImageUrls(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowImageUrls(1, 3) }
    }

    @Test
    fun `getTvShowsByGenre throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowsByGenre(any(), 1) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowsByGenre(1, 1) }
    }

    @Test
    fun `getTvShowCast throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowCast(any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowCast(1) }
    }

    @Test
    fun `getTvShowSeasonDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } throws ConnectionException()
        assertThrows<NoNetworkException> { repository.getTvShowSeason(1, 1) }
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
        assertThrows<NoNetworkException> { repository.getTvShowTrailer(1) }
    }

    @Test
    fun `getTvShowSeasonDetails returns season entity`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } returns dummySeasonDto
        val result = repository.getTvShowSeason(1, 1)
        assertEquals("Season 1", result.title)
    }

    @Test
    fun `getTvShowSeasonDetails throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowSeasonDetails(any(), any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowSeason(1, 99) }
    }

    // --- Episode ---
    @Test
    fun `getEpisodeDetails returns episode entity`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } returns dummyEpisodeDto
        val result = repository.getEpisodeDetails(1, 1, 1)
        assertEquals("Episode", result.title)
    }

    @Test
    fun `getEpisodeDetails throws NovixAppException`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getEpisodeDetails(1, 1, 999) }
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
    fun `getEpisodeImageUrls throws NovixAppException`() = runTest {
        coEvery { remote.getEpisodeImageUrls(any(), any(), any()) } throws Exception()
        assertThrows<NovixAppException> {
            repository.getEpisodeImageUrls(
                1,
                1,
                99,
                11
            )
        }
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws NovixAppException`() = runTest {
        coEvery { remote.getEpisodeGuestsOfHonor(any(), any(), any()) } throws Exception()
        assertThrows<NovixAppException> {
            repository.getEpisodeGuestsOfHonor(
                1, 1, 99
            )
        }
    }

    @Test
    fun `getTvShowVideosUrls returns null if no videos`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } returns emptyList()
        val result = repository.getTvShowTrailer(1)
        assertNull(result)
    }

    @Test
    fun `getTvShowVideosUrls throws NovixAppException`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } throws Exception()
        assertThrows<NovixAppException> { repository.getTvShowTrailer(404) }
    }

    @Test
    fun `getTvShowVideosUrls return url if there is a video`() = runTest {
        coEvery { remote.getTvShowVideosUrls(any()) } returns listOf(
            VideoDto(
                id = "1", key = "xyz", site = "YouTube", type = "Trailer"
            )
        )
        val result = repository.getTvShowTrailer(1)
        assertNotNull(result)

    }

    @Test
    fun `getPopularTvShows should return cached data if available when page is 1`() = runTest {
        val cached = listOf(sampleTvShowLocalDto)
        coEvery { local.getCachedTvShows(Category.POPULAR_MEDIA) } returns cached

        val result = repository.getPopularTvShows(1)

        assertThat(result).isEqualTo(cached.map { it.toEntity() })
    }

    @Test
    fun `getPopularTvShows should fetches remote when page is 1 and cache is empty`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { local.getCachedTvShows(Category.POPULAR_MEDIA) } returns emptyList()
        coEvery { remote.fetchPopularTvShows(1) } returns tvShows

        val result = repository.getPopularTvShows(1)

        assertThat(result).isEqualTo(tvShows.map { it.toEntity() })
    }

    @Test
    fun `getPopularTvShows should cache the new data when page is 1 and cache is empty`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { local.getCachedTvShows(Category.POPULAR_MEDIA) } returns emptyList()
        coEvery { remote.fetchPopularTvShows(1) } returns tvShows

        repository.getPopularTvShows(1)

        coVerify(exactly = tvShows.size) { local.cacheTvShow(any(), any()) }
    }

    @Test
    fun `getPopularTvShows should fetch from remote if page is not 1`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { remote.fetchPopularTvShows(2) } returns tvShows

        val result = repository.getPopularTvShows(2)

        assertThat(result).isEqualTo(tvShows.map { it.toEntity() })
    }

    @Test
    fun `getPopularTvShows should not cache the new data when page is not 1`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { remote.fetchPopularTvShows(2) } returns tvShows

        repository.getPopularTvShows(2)

        coVerify(exactly = 0) { local.cacheTvShow(any(), any()) }
    }

    @Test
    fun `getTopRatedTvShows should return cached data if available when page is 1 and genreId is null`() =
        runTest {
            val cached = listOf(sampleTvShowLocalDto)
            coEvery { local.getCachedTvShows(Category.TOP_RATED_MEDIA) } returns cached

            val result = repository.getTopRatedTvShows(1, null)

            assertThat(result).isEqualTo(cached.map { it.toEntity() })
        }

    @Test
    fun `getTopRatedTvShows should fetches remote when page is 1 and genreId is null and cache is empty`() =
        runTest {
            val tvShows = listOf(sampleTvShowDto)
            coEvery { local.getCachedTvShows(Category.TOP_RATED_MEDIA) } returns emptyList()
            coEvery { remote.fetchTopRatedTvShows(1, null) } returns tvShows

            val result = repository.getTopRatedTvShows(1, null)

            assertThat(result).isEqualTo(tvShows.map { it.toEntity() })
        }

    @Test
    fun `getTopRatedTvShows should cache the new data when page is 1 and genreId is null and cache is empty`() =
        runTest {
            val tvShows = listOf(sampleTvShowDto)
            coEvery { local.getCachedTvShows(Category.TOP_RATED_MEDIA) } returns emptyList()
            coEvery { remote.fetchTopRatedTvShows(1, null) } returns tvShows

            repository.getTopRatedTvShows(1, null)

            coVerify(exactly = tvShows.size) { local.cacheTvShow(any(), any()) }
        }

    @Test
    fun `getTopRatedTvShows should not cache the new data when page is not 1`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { local.getCachedTvShows(Category.TOP_RATED_MEDIA) } returns emptyList()
        coEvery { remote.fetchTopRatedTvShows(2, null) } returns tvShows

        repository.getTopRatedTvShows(2, null)

        coVerify(exactly = 0) { local.cacheTvShow(any(), any()) }
    }

    @Test
    fun `getTopRatedTvShows should not cache the new data when genreId is not null`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { local.getCachedTvShows(Category.TOP_RATED_MEDIA) } returns emptyList()
        coEvery { remote.fetchTopRatedTvShows(1, 1) } returns tvShows

        repository.getTopRatedTvShows(1, 1)

        coVerify(exactly = 0) { local.cacheTvShow(any(), any()) }
    }

    @Test
    fun `getTopRatedTvShows should fetch from remote when page is not 1`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { remote.fetchTopRatedTvShows(2, null) } returns tvShows

        val result = repository.getTopRatedTvShows(2, null)

        assertThat(result).isEqualTo(tvShows.map { it.toEntity() })
    }

    @Test
    fun `getTopRatedTvShows should fetch from remote when genreId is not null`() = runTest {
        val tvShows = listOf(sampleTvShowDto)
        coEvery { remote.fetchTopRatedTvShows(1, 1) } returns tvShows

        val result = repository.getTopRatedTvShows(1, 1)

        assertThat(result).isEqualTo(tvShows.map { it.toEntity() })
    }

    @Test
    fun `getTrendingTvShows returns empty list`() = runTest {
        // Act
        val result = repository.getTrendingTvShows(1, null)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getTvShowRate should return rate when available`() = runTest {
        val id = 1
        val accountId = 1L
        val sessionId = flowOf("session123")
        val rate = 8.5f
        val rates = listOf(TvShowDto(id = id, rating = rate))
        coEvery { preferences.sessionId } returns sessionId
        coEvery { remote.getTvShowRate(accountId, sessionId.first()) } returns rates

        val result = repository.getTvShowRate(accountId, id)

        assertThat(result).isEqualTo(rate.toInt())
    }

    @Test
    fun `getTvShowRate should returns rate null when no rate is available`() = runTest {
        val id = 1
        val accountId = 1L
        val sessionId = flowOf("session123")
        coEvery { preferences.sessionId } returns sessionId
        coEvery { remote.getTvShowRate(accountId, sessionId.first()) } returns emptyList()

        val result = repository.getTvShowRate(accountId, id)

        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getEpisodesRate should return rate when available`() = runTest {
        val episodeNumber = 1
        val seasonNumber = 1
        val accountId = 1L
        val sessionId = flowOf("session123")
        val rate = 8.5f
        val rates = listOf(
            EpisodeDto(
                episodeNumber = episodeNumber,
                seasonNumber = seasonNumber,
                rating = rate
            )
        )
        coEvery { preferences.sessionId } returns sessionId
        coEvery { remote.getEpisodesRate(accountId, sessionId.first()) } returns rates

        val result = repository.getEpisodesRate(accountId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(rate.toInt())
    }

    @Test
    fun `getEpisodesRate should returns rate null when no rate is available`() = runTest {
        val episodeNumber = 1
        val seasonNumber = 1
        val accountId = 1L
        val sessionId = flowOf("session123")
        coEvery { preferences.sessionId } returns sessionId
        coEvery { remote.getEpisodesRate(accountId, sessionId.first()) } returns emptyList()

        val result = repository.getEpisodesRate(accountId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getTvShowGenres should return cached data when available`() = runTest {
        coEvery { local.getCachedGenres(Category.TV_SHOW_GENRES) } returns genres

        val result = repository.getTvShowGenres()

        assertThat(result).isEqualTo(genres.map { it.toEntity() })
    }

    @Test
    fun `getTvShowGenres should fetches remote when cache is empty`() = runTest {
        coEvery { local.getCachedGenres(Category.TV_SHOW_GENRES) } returns emptyList()
        coEvery { remote.getTvShowGenres() } returns dummyGenresDto

        val result = repository.getTvShowGenres()

        assertThat(result).isEqualTo(dummyGenresDto.map { it.toEntity() })
    }

    @Test
    fun `getTvShowGenres should cache the new data when cache is empty`() = runTest {
        coEvery { local.getCachedGenres(Category.TV_SHOW_GENRES) } returns emptyList()
        coEvery { remote.getTvShowGenres() } returns dummyGenresDto

        repository.getTvShowGenres()

        coVerify(exactly = 1) { local.cacheGenres(any(), any()) }
    }

    private companion object {
        val dummyReviewDto = ReviewDto(
            id = "1", content = "Review", authorDetails = AuthorDetailsDto(
                name = "A", username = "a", avatarPath = "/avatar.png", rating = 4.0f
            ), createdAt = "2024-01-01"
        )

        val dummyActorDto = ActorDto(
            id = 10, name = "Actor", gender = 2,
        )

        val dummySeasonDto = SeasonDto(
            id = 5, name = "Season 1", overview = "intro", seasonNumber = 1, episodes = emptyList()
        )

        val dummyEpisodeDto = EpisodeDto(
            id = 8,
            name = "Episode",
            overview = "desc",
            seasonNumber = 1,
            episodeNumber = 1,
            voteAverage = 8f,
            airDate = "2024-01-01",
            runtime = 50
        )

        val sampleTvShowDto = TvShowDto(id = 1, name = "Fight club")
        val sampleTvShowLocalDto = TvShowLocalDto(
            id = 1, title = "Fight club",
            posterImageUrl = "",
            releaseDate = null,
            imdbRating = 5f
        )
        val dummyGenresDto = listOf(
            GenreDto(id = 1, name = "Action"),
            GenreDto(id = 2, name = "Drama")
        )

        val dummyGenre1 = GenreLocalDto(1, "Action")
        val dummyGenre2 = GenreLocalDto(2, "Crime")
        val genres = listOf(dummyGenre1, dummyGenre2)
    }
}