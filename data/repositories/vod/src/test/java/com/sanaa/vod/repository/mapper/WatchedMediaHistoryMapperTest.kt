package com.sanaa.vod.repository.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.repository.mapper.history.toDto
import com.sanaa.vod.repository.mapper.history.toEntity
import entity.Genre
import entity.MediaHistoryItem
import org.junit.jupiter.api.Test
import usecase.search.search_param.MediaType

class WatchedMediaHistoryMapperTest {

    @Test
    fun `toDto should map MediaHistoryItem to WatchedMediaHistoryLocalDto correctly`() {
        // Given
        val mediaHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(Genre(id = 28, name = "Action"), Genre(id = 12, name = "Adventure")),
            isSaved = true
        )
        val username = "testuser"
        val timestamp = 1234567890L

        // When
        val result = mediaHistoryItem.toDto(username, timestamp)

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.username).isEqualTo("testuser")
        assertThat(result.posterImageUrl).isEqualTo("test_url")
        assertThat(result.mediaType).isEqualTo("MOVIE")
        assertThat(result.genres).isEqualTo(",28,12,")
        assertThat(result.isSaved).isTrue()
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `toDto should handle empty genres list`() {
        // Given
        val mediaHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = emptyList(),
            isSaved = false
        )
        val username = "testuser"

        // When
        val result = mediaHistoryItem.toDto(username)

        // Then
        assertThat(result.genres).isEqualTo("")
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `toEntity should map WatchedMediaHistoryLocalDto to MediaHistoryItem correctly`() {
        // Given
        val dto = WatchedMediaHistoryLocalDto(
            id = 1,
            username = "testuser",
            posterImageUrl = "test_url",
            mediaType = "MOVIE",
            genres = ",28,12,",
            isSaved = true,
            timestamp = 1234567890L
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.posterImageUrl).isEqualTo("test_url")
        assertThat(result.mediaType).isEqualTo(MediaType.MOVIE)
        assertThat(result.genres).hasSize(2)
        assertThat(result.genres[0].id).isEqualTo(28)
        assertThat(result.genres[0].name).isEqualTo("Action")
        assertThat(result.genres[1].id).isEqualTo(12)
        assertThat(result.genres[1].name).isEqualTo("Adventure")
        assertThat(result.isSaved).isTrue()
        assertThat(result.lastWatchedAt).isNotNull()
    }

    @Test
    fun `toEntity should handle TV series type correctly`() {
        // Given
        val dto = WatchedMediaHistoryLocalDto(
            id = 2,
            username = "testuser",
            posterImageUrl = "test_series_url",
            mediaType = "TV_SERIES",
            genres = ",16,35,",
            isSaved = false,
            timestamp = 1234567890L
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.mediaType).isEqualTo(MediaType.TV_SERIES)
        assertThat(result.isSaved).isFalse()
    }

    @Test
    fun `toEntity should handle empty genres string`() {
        // Given
        val dto = WatchedMediaHistoryLocalDto(
            id = 1,
            username = "testuser",
            posterImageUrl = "test_url",
            mediaType = "MOVIE",
            genres = "",
            isSaved = false,
            timestamp = 1234567890L
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `toEntity should handle malformed genres string`() {
        // Given
        val dto = WatchedMediaHistoryLocalDto(
            id = 1,
            username = "testuser",
            posterImageUrl = "test_url",
            mediaType = "MOVIE",
            genres = "invalid,format",
            isSaved = false,
            timestamp = 1234567890L
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `toEntity should handle single genre`() {
        // Given
        val dto = WatchedMediaHistoryLocalDto(
            id = 1,
            username = "testuser",
            posterImageUrl = "test_url",
            mediaType = "MOVIE",
            genres = ",28,",
            isSaved = false,
            timestamp = 1234567890L
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.genres).hasSize(1)
        assertThat(result.genres[0].id).isEqualTo(28)
        assertThat(result.genres[0].name).isEqualTo("Action")
    }

    @Test
    fun `round trip conversion should preserve data`() {
        // Given
        val originalItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(Genre(id = 28, name = "Action"), Genre(id = 12, name = "Adventure")),
            isSaved = true
        )
        val username = "testuser"

        // When
        val dto = originalItem.toDto(username)
        val convertedBack = dto.toEntity()

        // Then
        assertThat(convertedBack.id).isEqualTo(originalItem.id)
        assertThat(convertedBack.posterImageUrl).isEqualTo(originalItem.posterImageUrl)
        assertThat(convertedBack.mediaType).isEqualTo(originalItem.mediaType)
        assertThat(convertedBack.isSaved).isEqualTo(originalItem.isSaved)
        assertThat(convertedBack.genres).hasSize(originalItem.genres.size)
        assertThat(convertedBack.genres[0].id).isEqualTo(originalItem.genres[0].id)
        assertThat(convertedBack.genres[1].id).isEqualTo(originalItem.genres[1].id)
    }
} 