package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageMapperTest {

    @Test
    fun `toEntity prepends base URL to filePath`() {
        val imageDto = ImageDto(filePath = "/abc123.jpg")

        val result = imageDto.toEntity()

        assertEquals("https://image.tmdb.org/t/p/w500/abc123.jpg", result)
    }

    @Test
    fun `toEntity works with filePath without leading slash`() {
        val imageDto = ImageDto(filePath = "xyz789.png")

        val result = imageDto.toEntity()

        assertEquals("https://image.tmdb.org/t/p/w500xyz789.png", result)
    }
}