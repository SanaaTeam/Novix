package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import org.junit.jupiter.api.Test

class MovieVideoMapperTest {

    @Test
    fun `toDomain returns YouTube trailer url when trailer is present`() {
        val videos = listOf(
            MovieVideoDto(key = "abc123", site = "YouTube", type = "Trailer"),
            MovieVideoDto(key = "def456", site = "YouTube", type = "Teaser")
        )

        val result = videos.toDomain()

        assertThat(result).isEqualTo("https://www.youtube.com/watch?v=abc123")
    }

    @Test
    fun `toDomain returns null when no YouTube trailer present`() {
        val videos = listOf(
            MovieVideoDto(key = "abc123", site = "Vimeo", type = "Trailer"),
            MovieVideoDto(key = "def456", site = "YouTube", type = "Teaser")
        )

        val result = videos.toDomain()

        assertThat(result).isNull()
    }

    @Test
    fun `toDomain returns null when list is empty`() {
        val videos = emptyList<MovieVideoDto>()

        val result = videos.toDomain()

        assertThat(result).isNull()
    }
}