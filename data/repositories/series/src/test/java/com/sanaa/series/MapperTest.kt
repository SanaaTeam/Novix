package com.sanaa.series

import com.google.common.truth.Truth.assertThat
import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.AuthorDetailsDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.TvSeriesImageDto
import com.sanaa.series.dto.TvSeriesVideoDto
import com.sanaa.series.mapper.apiGenderMapping
import com.sanaa.series.mapper.buildPosterUrl
import com.sanaa.series.mapper.getFullImageUrl
import com.sanaa.series.mapper.toDtoId
import com.sanaa.series.mapper.toEntity
import entity.Actor
import entity.Genre
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapperTest {


    @Test
    fun `should map title when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(name = "My Series").toEntity()
        assertThat(result.title).isEqualTo("My Series")
    }

    @Test
    fun `should map seasonsCount when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(seasonsCount = 3).toEntity()
        assertThat(result.seasonsCount).isEqualTo(3)
    }

    @Test
    fun `should map overview when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(overview = "Overview text").toEntity()
        assertThat(result.overview).isEqualTo("Overview text")
    }

    @Test
    fun `should map firstAirDate when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(firstAirDate = "2023-06-01").toEntity()
        assertEquals(LocalDate.parse("2023-06-01"), result.releaseDate)
    }

    @Test
    fun `should map voteAverage when TvSeriesDto is mapped`() {

        val result = createTvSeriesDto(voteAverage = 8.5f).toEntity()
        assertThat(result.imdbRating).isEqualTo(8.5f)
    }

    @Test
    fun `should map posterPath when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(posterPath = "/poster.jpg").toEntity()
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterImageUrl)
    }

    @Test
    fun `should map genres when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(
            genres = listOf(
                GenreDto(28, "Action"),
                GenreDto(35, "Comedy")
            )
        ).toEntity()
        assertTrue(result.genres.contains(Genre.ACTION))
        assertTrue(result.genres.contains(Genre.COMEDY))
    }




//SeasonDto
@Test fun `SeasonDto toEntity maps id correctly`() {
    val dto = createSeasonDto(id = 10)
    val entity = dto.toEntity()
    assertEquals(10, entity.id)
}

    @Test fun `SeasonDto toEntity maps title correctly`() {
        val dto = createSeasonDto(name = "Season 1")
        val entity = dto.toEntity()
        assertEquals("Season 1", entity.title)
    }

    @Test fun `SeasonDto toEntity maps overview correctly`() {
        val dto = createSeasonDto(overview = "Season overview")
        val entity = dto.toEntity()
        assertEquals("Season overview", entity.overview)
    }

    @Test fun `SeasonDto toEntity maps season number correctly`() {
        val dto = createSeasonDto(seasonNumber = 1)
        val entity = dto.toEntity()
        assertEquals(1, entity.number)
    }

    @Test fun `SeasonDto toEntity maps episodes size correctly`() {
        val dto = createSeasonDto(episodes = listOf(createEpisodeDto()))
        val entity = dto.toEntity()
        assertEquals(1, entity.episodes.size)
    }

    @Test fun `SeasonDto toEntity maps episode title correctly`() {
        val dto = createSeasonDto(episodes = listOf(createEpisodeDto(name = "Ep 1")))
        val entity = dto.toEntity()
        assertEquals("Ep 1", entity.episodes[0].title)
    }

    //EpisodeDto
    @Test fun `EpisodeDto toEntity maps id correctly`() {
        val dto = createEpisodeDto(id = 100)
        val entity = dto.toEntity()
        assertEquals(100, entity.id)
    }

    @Test fun `EpisodeDto toEntity maps title correctly`() {
        val dto = createEpisodeDto(name = "Episode 1")
        val entity = dto.toEntity()
        assertEquals("Episode 1", entity.title)
    }

    @Test fun `EpisodeDto toEntity maps overview correctly`() {
        val dto = createEpisodeDto(overview = "Some overview")
        val entity = dto.toEntity()
        assertEquals("Some overview", entity.overview)
    }

    @Test fun `EpisodeDto toEntity maps seasonNumber correctly`() {
        val dto = createEpisodeDto(seasonNumber = 2)
        val entity = dto.toEntity()
        assertEquals(2, entity.seasonNumber)
    }

    @Test fun `EpisodeDto toEntity maps episode number correctly`() {
        val dto = createEpisodeDto(episodeNumber = 3)
        val entity = dto.toEntity()
        assertEquals(3, entity.number)
    }

    @Test fun `EpisodeDto toEntity maps imdbRating correctly`() {
        val dto = createEpisodeDto(voteAverage = 8.2f)
        val entity = dto.toEntity()
        assertEquals(8.2f, entity.imdbRating)
    }

    @Test fun `EpisodeDto toEntity maps duration correctly`() {
        val dto = createEpisodeDto(runtime = 60)
        val entity = dto.toEntity()
        assertEquals(60, entity.durationMinutes)
    }

    @Test fun `EpisodeDto toEntity maps releaseDate correctly`() {
        val dto = createEpisodeDto(airDate = "2023-04-20")
        val entity = dto.toEntity()
        assertEquals(LocalDate.parse("2023-04-20"), entity.releaseDate)
    }


    private fun createSeasonDto(
        id: Int = 10,
        name: String = "Season 1",
        overview: String = "Season overview",
        seasonNumber: Int = 1,
        episodes: List<EpisodeDto> = listOf(createEpisodeDto())
    ) = SeasonDto(
        id = id,
        name = name,
        overview = overview,
        seasonNumber = seasonNumber,
        episodes = episodes
    )

    private fun createEpisodeDto(
        id: Int = 1,
        name: String = "Ep 1",
        overview: String = "Desc",
        seasonNumber: Int = 1,
        episodeNumber: Int = 1,
        voteAverage: Float = 7.0f,
        airDate: String = "2023-01-01",
        runtime: Int = 60
    ) = EpisodeDto(
        id = id,
        name = name,
        overview = overview,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        voteAverage = voteAverage,
        airDate = airDate,
        runtime = runtime
    )
    private fun createTvSeriesDto(
        id: Int = 1,
        name: String = "My Series",
        overview: String = "Overview text",
        posterPath: String = "/poster.jpg",
        voteAverage: Float = 8.5f,
        firstAirDate: String = "2023-06-01",
        genres: List<GenreDto> = listOf(GenreDto(28, "Action"), GenreDto(35, "Comedy")),
        seasonsCount: Int = 3
    ) = TvSeriesDto(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        voteAverage = voteAverage,
        firstAirDate = firstAirDate,
        genres = genres,
        seasonsCount = seasonsCount
    )

}
