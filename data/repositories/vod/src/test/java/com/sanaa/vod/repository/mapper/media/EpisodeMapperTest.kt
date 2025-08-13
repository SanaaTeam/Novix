package com.sanaa.vod.repository.mapper.media


import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.util.DateTimeUtils.defaultDate
import entity.Episode
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class EpisodeMapperTest {

    @Test
    fun `toEntity maps EpisodeDto to Episode correctly`() {
        val dto = EpisodeDto(
            id = 100,
            name = "Pilot",
            overview = "The beginning of everything.",
            seasonNumber = 1,
            episodeNumber = 1,
            runtime = 55,
            airDate = "2023-10-15",
            stillPath = "/pilot.jpg"
        )

        val episode: Episode = dto.toEntity()

        assertEquals(100, episode.id)
        assertEquals("Pilot", episode.title)
        assertEquals("The beginning of everything.", episode.overview)
        assertEquals(1, episode.seasonNumber)
        assertEquals(1, episode.number)
        assertEquals(55, episode.durationMinutes)
        assertEquals(LocalDate(2023, 10, 15), episode.releaseDate)
        assertEquals("https://image.tmdb.org/t/p/w500/pilot.jpg", episode.stillImagePath)
    }

    @Test
    fun `toEntity returns default release date when airDate is null`() {
        val dto = EpisodeDto(
            id = 101,
            name = "No Date Episode",
            overview = "Missing air date.",
            seasonNumber = 1,
            episodeNumber = 2,
            runtime = 50,
            airDate = null,
            stillPath = null
        )

        val episode = dto.toEntity()

        assertEquals(defaultDate, episode.releaseDate)
    }
}