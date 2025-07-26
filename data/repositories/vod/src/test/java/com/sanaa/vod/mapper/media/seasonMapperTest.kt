package com.sanaa.vod.mapper.media


import com.sanaa.vod.dataSource.remote.dto.EpisodeDto
import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import entity.Season
import kotlin.test.Test
import kotlin.test.assertEquals

class SeasonMapperTest {

    @Test
    fun `toEntity maps SeasonDto to Season with episodes`() {
        val episode1 = EpisodeDto(
            id = 1,
            name = "Episode One",
            overview = "First episode description.",
            stillPath = "/ep1.jpg",
            airDate = "2024-01-01",
            episodeNumber = 1
        )
        val episode2 = EpisodeDto(
            id = 2,
            name = "Episode Two",
            overview = "Second episode description.",
            stillPath = "/ep2.jpg",
            airDate = "2024-01-08",
            episodeNumber = 2
        )
        val seasonDto = SeasonDto(
            id = 101,
            name = "Season 1",
            overview = "Season 1 overview.",
            seasonNumber = 1,
            episodes = listOf(episode1, episode2)
        )

        val result: Season = seasonDto.toEntity()

        assertEquals(101, result.id)
        assertEquals("Season 1", result.title)
        assertEquals("Season 1 overview.", result.overview)
        assertEquals(1, result.number)
        assertEquals(2, result.episodes.size)

        val firstEpisode = result.episodes[0]
        assertEquals(1, firstEpisode.id)
        assertEquals("Episode One", firstEpisode.title)
        assertEquals("First episode description.", firstEpisode.overview)
        assertEquals(1, firstEpisode.number)
    }
}