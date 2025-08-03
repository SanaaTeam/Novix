package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.VideoDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VideoMapperTest {

    @Test
    fun `toDomain returns YouTube trailer URL when present`() {
        val videos = listOf(
            VideoDto(key = "abc123", site = "YouTube", type = "Trailer"),
            VideoDto(key = "def456", site = "Vimeo", type = "Trailer"),
            VideoDto(key = "ghi789", site = "YouTube", type = "Teaser")
        )

        val url = videos.toDomain()

        assertEquals("https://www.youtube.com/watch?v=abc123", url)
    }

    @Test
    fun `toDomain returns null when no YouTube trailer found`() {
        val videos = listOf(
            VideoDto(key = "def456", site = "Vimeo", type = "Trailer"),
            VideoDto(key = "ghi789", site = "YouTube", type = "Teaser")
        )

        val url = videos.toDomain()

        assertNull(url)
    }

    @Test
    fun `toDomain returns null when list is empty`() {
        val videos = emptyList<VideoDto>()

        val url = videos.toDomain()

        assertNull(url)
    }
}