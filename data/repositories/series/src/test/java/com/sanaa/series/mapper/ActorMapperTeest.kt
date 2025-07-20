package com.sanaa.series.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.series.dto.ActorDto
import entity.Actor
import kotlin.test.Test

class ActorMapperTeest {
    @Test
    fun `should map gender to MALE when gender id is 0`() {
        val result = createActorDto(gender = 0).toEntity()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender to FEMALE when gender id is 1`() {
        val result = createActorDto(gender = 1).toEntity()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should default gender to MALE when gender id is unknown`() {
        val result = createActorDto(gender = 99).toEntity()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map imageUrl when profilePath is valid`() {
        val result = createActorDto(profilePath = "/abc.jpg").toEntity()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/abc.jpg")
    }

    @Test
    fun `should return empty imageUrl when profilePath is null`() {
        val result = createActorDto(profilePath = null).toEntity()
        assertThat(result.imageUrl).isEmpty()
    }

    @Test
    fun `should return empty imageUrl when profilePath is blank`() {
        val result = createActorDto(profilePath = "").toEntity()
        assertThat(result.imageUrl).isEmpty()
    }

    private fun createActorDto(
        id: Int = 1,
        name: String = "Default Name",
        character: String = "Default Character",
        profilePath: String? = "/default.jpg",
        gender: Int = 0
    ): ActorDto {
        return ActorDto(
            id = id,
            name = name,
            character = character,
            profilePath = profilePath,
            gender = gender
        )
    }
}