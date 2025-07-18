package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.CastDto
import entity.Actor
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `should return correct name when ActorDto has valid name`() {
        val dto = ActorDto(
            id = 1,
            name = "Robert",
            profileImagePath = null,
            gender = null,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Robert")
    }

    @Test
    fun `should return Unknown name when ActorDto name is null`() {
        val dto = ActorDto(
            id = 1,
            name = null,
            profileImagePath = null,
            gender = null,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Unknown")
    }

    @Test
    fun `should return imageUrl from profile path in ActorDto`() {
        val dto = ActorDto(
            id = 1,
            name = "Any",
            profileImagePath = "/pic.jpg",
            gender = null,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w185/pic.jpg")
    }

    @Test
    fun `should map gender as FEMALE when ActorDto gender is 1`() {
        val dto = ActorDto(
            id = 1,
            name = "Any",
            profileImagePath = null,
            gender = 1,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should map gender as MALE when ActorDto gender is 0`() {
        val dto = ActorDto(
            id = 1,
            name = "Any",
            profileImagePath = null,
            gender = 0,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as MALE when ActorDto gender is unknown`() {
        val dto = ActorDto(
            id = 1,
            name = "Any",
            profileImagePath = null,
            gender = 99,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map biography to empty when ActorDto biography is null`() {
        val dto = ActorDto(
            id = 1,
            name = "Any",
            profileImagePath = null,
            gender = null,
            character = null,
            biography = null
        )
        val result = dto.toDomain()
        assertThat(result.biography).isEqualTo("")
    }

    @Test
    fun `should return correct name when CastDto has valid name`() {
        val dto = CastDto.Cast(
            id = 2,
            name = "Scarlett",
            profilePath = null,
            gender = null,
            character = null
        )
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Scarlett")
    }

    @Test
    fun `should return Unknown name when CastDto name is null`() {
        val dto =
            CastDto.Cast(id = 2, name = null, profilePath = null, gender = null, character = null)
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Unknown")
    }

    @Test
    fun `should return imageUrl from profile path in CastDto`() {
        val dto = CastDto.Cast(
            id = 2,
            name = "Any",
            profilePath = "/cast.jpg",
            gender = null,
            character = null
        )
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w185/cast.jpg")
    }

    @Test
    fun `should map gender as FEMALE when CastDto gender is 1`() {
        val dto =
            CastDto.Cast(id = 2, name = "Any", profilePath = null, gender = 1, character = null)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should return correct URL when getProfileImageUrl is called with path`() {
        val url = getProfileImageUrl("/image.jpg")
        assertThat(url).isEqualTo("https://image.tmdb.org/t/p/w185/image.jpg")
    }

    @Test
    fun `should map gender as MALE when CastDto gender is null`() {
        val dto =
            CastDto.Cast(id = 2, name = "Any", profilePath = null, gender = null, character = null)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as MALE when gender value is unrecognized`() {
        val gender = apiGenderMapping(99)
        assertThat(gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as MALE when gender is 0`() {
        val gender = apiGenderMapping(0)
        assertThat(gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as FEMALE when gender is 1`() {
        val gender = apiGenderMapping(1)
        assertThat(gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should map gender as MALE when gender is null`() {
        val gender = apiGenderMapping(null)
        assertThat(gender).isEqualTo(Actor.Gender.MALE)
    }
}