package com.sanaa.vod.repository.mapper.media

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import entity.Genre
import org.junit.jupiter.api.Test

class GenreMapperTest {

    @Test
    fun `toDto maps Genre to GenreDto correctly`() {
        val genre = Genre(id = 28, name = "Action")

        val dto = genre.toDto()

        assertThat(dto.id).isEqualTo(28)
    }

    @Test
    fun `toEntity maps GenreDto to Genre correctly`() {
        val dto = GenreDto(id = 35, name = "Comedy")

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(35)
        assertThat(entity.name).isEqualTo("Comedy")
    }

    @Test
    fun `toEntity sets default id to 0 when GenreDto id is null`() {
        val dto = GenreDto(id = null, name = "Drama")

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(0)
        assertThat(entity.name).isEqualTo("Drama")
    }

    @Test
    fun `toEntity sets empty name when GenreDto name is null`() {
        val dto = GenreDto(id = 15, name = null)

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(15)
        assertThat(entity.name).isEqualTo("")
    }
}