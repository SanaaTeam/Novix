package com.sanaa.vod.repository.mapper.history

import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.util.DateTimeUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import usecase.search.ManageRecentViewedUseCase
import usecase.search.search_param.MediaType

class MapRecentViewedKtTest {

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map id correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        Assertions.assertEquals(1, dto.id)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map imageUrl correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        Assertions.assertEquals("imageUrl", dto.imageUrl)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map isSaved correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        Assertions.assertEquals(true, dto.isSaved)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map mediaType correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        Assertions.assertEquals("MOVIE", dto.mediaType)
    }

    @Test
    fun `given RecentViewedMedia when toDto called with timestamp should map timestamp correctly`() {
        val dto = createRecentViewedMedia().toDto(1234567890L)
        Assertions.assertEquals(1234567890L, dto.timestamp)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map id correctly`() {
        val result = createRecentViewedMedia().toDto()
        Assertions.assertEquals(1, result.id)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map imageUrl correctly`() {
        val result = createRecentViewedMedia().toDto()
        Assertions.assertEquals("imageUrl", result.imageUrl)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map isSaved correctly`() {
        val result = createRecentViewedMedia().toDto()
        Assertions.assertEquals(true, result.isSaved)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should map mediaType correctly`() {
        val result = createRecentViewedMedia().toDto()
        Assertions.assertEquals("MOVIE", result.mediaType)
    }

    @Test
    fun `given RecentViewedMedia when toDto called without timestamp should use current timestamp`() {
        val result = createRecentViewedMedia().toDto()
        val now = DateTimeUtils.getCurrentTimeStamp()
        Assertions.assertTrue(now - result.timestamp < 1000)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map id correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        Assertions.assertEquals(1, entity.id)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map posterImageUrl correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        Assertions.assertEquals("imageUrl", entity.posterImageUrl)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map isSaved correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        Assertions.assertEquals(true, entity.isSaved)
    }

    @Test
    fun `given RecentViewedLocalDto when toEntity called should map mediaType correctly`() {
        val entity = createRecentViewedLocalDto().toEntity()
        Assertions.assertEquals(MediaType.MOVIE, entity.mediaType)
    }

    private fun createRecentViewedMedia(): ManageRecentViewedUseCase.RecentViewedMedia {
        return ManageRecentViewedUseCase.RecentViewedMedia(
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