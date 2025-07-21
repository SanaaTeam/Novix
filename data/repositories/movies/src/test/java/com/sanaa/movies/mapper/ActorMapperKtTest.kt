package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import entity.Actor.Gender
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toDomain maps male gender correctly`() {
        val dto = ActorDto(id = 1, name = "John", profilePath = "/john.jpg", gender = 0)
        val domain = dto.toDomain()
        assertThat(domain.gender).isEqualTo(Gender.MALE)
    }

    @Test
    fun `toDomain maps female gender correctly`() {
        val dto = ActorDto(id = 2, name = "Jane", profilePath = "/jane.jpg", gender = 1)
        val domain = dto.toDomain()
        assertThat(domain.gender).isEqualTo(Gender.FEMALE)
    }

    @Test
    fun `toDomain defaults to MALE when gender is unknown`() {
        val dto = ActorDto(id = 3, name = "Alex", profilePath = "/alex.jpg", gender = 999)
        val domain = dto.toDomain()
        assertThat(domain.gender).isEqualTo(Gender.MALE)
    }

    @Test
    fun `toDomain maps full image url correctly`() {
        val dto = ActorDto(id = 4, name = "Mike", profilePath = "/mike.png", gender = 0)
        val domain = dto.toDomain()
        assertThat(domain.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/mike.png")
    }

    @Test
    fun `toDomain returns empty string if profile path is null`() {
        val dto = ActorDto(id = 5, name = "Null Guy", profilePath = null, gender = 0)
        val domain = dto.toDomain()
        assertThat(domain.imageUrl).isEmpty()
    }

    @Test
    fun `toDomain returns empty string if profile path is blank`() {
        val dto = ActorDto(id = 6, name = "Blank Guy", profilePath = "   ", gender = 1)
        val domain = dto.toDomain()
        assertThat(domain.imageUrl).isEmpty()
    }
}