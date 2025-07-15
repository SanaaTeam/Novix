package com.sanaa.series

import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.AuthorDetailsDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.ImageDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.VideoDto
import details.repository.TvSeriesRepository
import entity.Genre
import exceptions.NotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class TvSeriesRepositoryImplTest {

    private lateinit var repository: TvSeriesRepository
    private val remote: RemoteTvSeriesDataSource = mockk(relaxed = true)

    private val dummyTvSeriesDto = TvSeriesDto(
        id = 1,
        name = "Test Series",
        overview = "overview",
        posterPath = "/img.jpg",
        voteAverage = 7f,
        seasonsCount = 2,
        genres = listOf(GenreDto(1, "Drama"))
    )

    private val dummyReviewDto = ReviewDto(
        id = "1",
        content = "Review",
        authorDetails = AuthorDetailsDto(
            name = "A",
            username = "a",
            avatarPath = "/avatar.png",
            rating = 4.0f
        ),
        createdAt = "2024-01-01"
    )

    private val dummyActorDto = ActorDto(
        id = 10,
        profilePath = "/pic.jpg",
        name = "Actor",
        gender = 2,
        character = "Hero"
    )

    private val dummySeasonDto = SeasonDto(
        id = 5,
        name = "Season 1",
        overview = "intro",
        seasonNumber = 1,
        episodes = emptyList()
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

    private val dummyVideoDto = VideoDto(
        id = "v1",
        key = "trailer_key",
        name = "Trailer",
        site = "YouTube",
        type = "Trailer"
    )

    @BeforeEach
    fun setup() {
        repository = TvSeriesRepositoryImpl(remote)
    }

    @Test
    fun `getTvSeriesDetails throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeries(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesDetails(999) }
    }

    @Test
    fun `getTvSeriesReviews returns list`() = runTest {
        coEvery { remote.getTvSeriesReviews(1) } returns listOf(dummyReviewDto)
        val result = repository.getTvSeriesReviews(1)
        assertEquals("A", result.first().authorName)
    }

    @Test
    fun `getTvSeriesReviews throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesReviews(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesReviews(99) }
    }

    @Test
    fun `getTvSeriesImages throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesImages(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesImages(99) }
    }

    @Test
    fun `getTvSeriesByGenre throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesByGenre(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesByGenre(Genre.CRIME) }
    }

    // --- Cast ---
    @Test
    fun `getTvSeriesCast returns actors`() = runTest {
        coEvery { remote.getTvSeriesCast(any()) } returns listOf(dummyActorDto)
        val result = repository.getTvSeriesCast(1)
        assertEquals("Actor", result.first().name)
    }

    @Test
    fun `getTvSeriesCast throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesCast(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesCast(999) }
    }

    // --- Season ---
    @Test
    fun `getTvSeriesSeason returns season entity`() = runTest {
        coEvery { remote.getTvSeriesSeasonDetails(any(), any()) } returns dummySeasonDto
        val result = repository.getTvSeriesSeason(1, 1)
        assertEquals("Season 1", result.title)
    }

    @Test
    fun `getTvSeriesSeason throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesSeasonDetails(any(), any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesSeason(1, 99) }
    }

    // --- Episode ---
    @Test
    fun `getEpisodeDetails returns episode entity`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } returns dummyEpisodeDto
        val result = repository.getEpisodeDetails(1, 1, 1)
        assertEquals("Episode", result.title)
    }

    @Test
    fun `getEpisodeDetails throws NotFoundException`() = runTest {
        coEvery { remote.getEpisodeDetails(any(), any(), any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getEpisodeDetails(1, 1, 999) }
    }

    @Test
    fun `getEpisodeImages returns list`() = runTest {
        coEvery { remote.getEpisodeImages(any(), any(), any()) } returns listOf(ImageDto("2"))
        val result = repository.getEpisodeImages(1, 1, 1)
        assertEquals(1, result.size)
    }

    @Test
    fun `getEpisodeImages throws NotFoundException`() = runTest {
        coEvery { remote.getEpisodeImages(any(), any(), any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getEpisodeImages(1, 1, 99) }
    }

    @Test
    fun `getEpisodeGuestsOfHonor returns list`() = runTest {
        coEvery {
            remote.getEpisodeGuestsOfHonor(
                any(),
                any(),
                any()
            )
        } returns listOf(dummyActorDto)
        val result = repository.getEpisodeGuestsOfHonor(1, 1, 1)
        assertEquals("Hero", result.first().character)
    }

    @Test
    fun `getEpisodeGuestsOfHonor throws NotFoundException`() = runTest {
        coEvery { remote.getEpisodeGuestsOfHonor(any(), any(), any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getEpisodeGuestsOfHonor(1, 1, 99) }
    }

    @Test
    fun `getTvSeriesTrailer returns null if no videos`() = runTest {
        coEvery { remote.getTvSeriesVideos(any()) } returns emptyList()
        val result = repository.getTvSeriesTrailer(1)
        assertNull(result)
    }

    @Test
    fun `getTvSeriesTrailer throws NotFoundException`() = runTest {
        coEvery { remote.getTvSeriesVideos(any()) } throws Exception()
        assertFailsWith<NotFoundException> { repository.getTvSeriesTrailer(404) }
    }
}