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

        val expected = Actor(
            id = 1,
            name = "Robert Downey Jr.",
            imageUrl = "/rdj.jpg",
            gender = Actor.Gender.MALE,
            character = "Iron Man",
            biography = "Bio",
            birthDate = null,
            deathDate = null,
            placeOfBirth = null,
            region = null,
            lastShow = null,
            department = null
        )

        val result = dto.toDomain()

        assertThat(result).isEqualTo(expected)
    }

}