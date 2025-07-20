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
        assertTrue(TimeUtils.getCurrentTimeStamp() - result.timestamp < 1000)
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