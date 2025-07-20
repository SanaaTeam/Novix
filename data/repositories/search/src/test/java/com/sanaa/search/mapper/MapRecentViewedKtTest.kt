package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import com.sanaa.search.util.TimeUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import search.usecase.search_param.MediaType
import search.usecase.search_param.RecentViewedMedia

class MapRecentViewedKtTest {

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map id correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        assertEquals(1, dto.id)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map imageUrl correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        assertEquals("imageUrl", dto.imageUrl)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map isSaved correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        assertEquals(true, dto.isSaved)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map mediaType correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        assertEquals("MOVIE", dto.mediaType)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map timestamp correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        assertEquals(1234567890L, dto.timestamp)
    }


    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map id correctly`() {
        val result = createRecentViewedMedia().toDto()
        assertEquals(1, result.id)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map imageUrl correctly`() {
        val result = createRecentViewedMedia().toDto()
        assertEquals("imageUrl", result.imageUrl)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map isSaved correctly`() {
        val result = createRecentViewedMedia().toDto()
        assertEquals(true, result.isSaved)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map mediaType correctly`() {
        val result = createRecentViewedMedia().toDto()
        assertEquals("MOVIE", result.mediaType)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should use current timestamp`() {
        val result = createRecentViewedMedia().toDto()
        val now = TimeUtils.getCurrentTimeStamp()
        assertTrue(now - result.timestamp < 1000)
    }
    @Test
    fun `given RecentViewedLocalDto when toEntity called should map id correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        assertEquals(1, entity.id)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map posterImageUrl correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        assertEquals("imageUrl", entity.posterImageUrl)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map isSaved correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        assertEquals(true, entity.isSaved)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map mediaType correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        assertEquals(MediaType.MOVIE, entity.mediaType)
    }

    private fun createRecentViewedMedia(): RecentViewedMedia {
        return RecentViewedMedia(
            id = 1,
            posterImageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE,
        )
    }

    private fun createRecentViewedLocalDto(timestamp: Long = 1234567890L): RecentViewedLocalDto {
        return RecentViewedLocalDto(
            id = 1,
            imageUrl = "imageUrl",
            isSaved = true,
            mediaType = MediaType.MOVIE.name,
            timestamp = timestamp
        )
    }

}