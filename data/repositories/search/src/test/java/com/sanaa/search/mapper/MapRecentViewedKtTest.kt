package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import usecase.search.MediaType
import usecase.search.RecentViewedMedia
import usecase.search.SearchHistory

class MapRecentViewedKtTest {

    @Test
    fun `toDto maps RecentViewedMedia to RecentViewedLocalDto`() {
        val recentViewedMedia = RecentViewedMedia(
            id = 1,
            posterImageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE,
        )
        val fixedTimestamp = 1234567890L

        val expectedDto = RecentViewedLocalDto(
            id = 1,
            imageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE.name,
            timestamp = fixedTimestamp
        )

        assertEquals(expectedDto, recentViewedMedia.toDto(fixedTimestamp))

    }

    @Test
    fun `toEntity maps RecentViewedLocalDto to RecentViewedMedia`() {
        val recentViewedLocalDto = RecentViewedLocalDto(
            id = 1,
            imageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE.name,
            timestamp = 1234567890L
        )
        val expectedMedia = RecentViewedMedia(
            id = 1,
            posterImageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE,
        )
        assertEquals(expectedMedia, recentViewedLocalDto.toEntity())
    }

    @Test
    fun `toEntity maps QueryLocalDto to SearchHistory`() {
        val queryLocalDto = QueryLocalDto(
            id = 1,
            query = "query",
            timestamp = 1234567890L
        )
        val expectedSearchHistory = SearchHistory(
            id = 1,
            query = "query",
            timestamp = Instant.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
        assertEquals(expectedSearchHistory, queryLocalDto.toEntity())
    }

    @Test
    fun `toDto uses current timestamp by default`() {
        val recentViewedMedia = RecentViewedMedia(
            id = 1,
            posterImageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE,
        )

        val result = recentViewedMedia.toDto()

        assertEquals(1, result.id)
        assertEquals("imageUrl", result.imageUrl)
        assertEquals(true, result.isSaved)
        assertEquals("MOVIE", result.mediaType)
        assertTrue(System.currentTimeMillis() - result.timestamp < 1000)
    }

}