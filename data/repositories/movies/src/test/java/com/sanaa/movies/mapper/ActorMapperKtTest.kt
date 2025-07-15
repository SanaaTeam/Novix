package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.CastDto
import entity.Actor
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `ActorDto toDomain maps correctly`() {
        val dto = ActorDto(
            id = 1,
            name = "Robert Downey Jr.",
            profileImagePath = "/rdj.jpg",
            gender = 2,
            character = "Iron Man",
            biography = "Bio"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Robert Downey Jr.")
        assertThat(result.imageUrl).isEqualTo("/rdj.jpg")
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
        assertThat(result.character).isEqualTo("Iron Man")
        assertThat(result.biography).isEqualTo("Bio")
    }

    @Test
    fun `CastDto toDomain uses fallback values`() {
        val dto = CastDto.Cast(
            id = null,
            name = null,
            profilePath = null,
            gender = 1,
            character = "Some Character"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(0)
        assertThat(result.name).isEqualTo("Unknown")
        assertThat(result.imageUrl).isEqualTo("")
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }
}