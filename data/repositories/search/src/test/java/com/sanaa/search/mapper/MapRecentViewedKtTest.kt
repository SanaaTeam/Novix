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