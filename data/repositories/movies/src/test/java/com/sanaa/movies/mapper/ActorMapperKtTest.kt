package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import entity.Actor
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `should return correct name when ActorDto has valid name`() {
        val dto = createActorDto(name = "Robert")
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Robert")
    }

    @Test
    fun `should return Unknown name when ActorDto name is null`() {
        val dto = createActorDto(name = null)
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Unknown")
    }

    @Test
    fun `should return imageUrl from profile path in ActorDto`() {
        val dto = createActorDto(profileImagePath = "/pic.jpg")
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/pic.jpg")
    }

    @Test
    fun `should map gender as FEMALE when ActorDto gender is 1`() {
        val dto = createActorDto(gender = 1)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should map gender as MALE when ActorDto gender is 0`() {
        val dto = createActorDto(gender = 0)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as MALE when ActorDto gender is unknown`() {
        val dto = createActorDto(gender = 99)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should return correct biography when ActorDto has valid biography`() {
        val dto = createActorDto(biography = "Any biography")
        val result = dto.toDomain()
        assertThat(result.biography).isEqualTo("Any biography")
    }

    @Test
    fun `should return Unknown biography  when ActorDto biography is null`() {
        val dto = createActorDto(biography = null)
        val result = dto.toDomain()
        assertThat(result.biography).isEqualTo("Unknown biography")
    }

    //CastDto
    @Test
    fun `should return correct name when CastDto has valid name`() {
        val dto = createCastDto(name = "Scarlett")
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Scarlett")
    }

    @Test
    fun `should return Unknown name when CastDto name is null`() {
        val dto =
            createCastDto()
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Unknown")
    }

    @Test
    fun `should return imageUrl from profile path in CastDto`() {
        val dto = createCastDto(profilePath = "/cast.jpg")
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/cast.jpg")
    }

    @Test
    fun `should map gender as FEMALE when CastDto gender is 1`() {
        val dto = createCastDto(gender = 1)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should return correct URL when getProfileImageUrl is called with path`() {
        val url = getFullImageUrl("/image.jpg")
        assertThat(url).isEqualTo("https://image.tmdb.org/t/p/w500/image.jpg")
    }

    @Test
    fun `should map gender as MALE when CastDto gender is null`() {
        val dto = createCastDto(gender = null)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    private fun createActorDto(
        id: Int = 1,
        name: String? = null,
        profileImagePath: String? = null,
        gender: Int? = null,
        character: String? = null,
        biography: String? = null
    ) = ActorDto(
        id = id,
        name = name,
        profileImagePath = profileImagePath,
        gender = gender,
        character = character,
        biography = biography
    )

    private fun createCastDto(
        id: Int = 2,
        name: String? = null,
        profilePath: String? = null,
        gender: Int? = null,
        character: String? = null
    ) = CastDto.Cast(
        id = id,
        name = name,
        profilePath = profilePath,
        gender = gender,
        character = character
    )


}