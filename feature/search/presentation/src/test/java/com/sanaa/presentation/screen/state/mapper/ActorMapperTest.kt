package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.ActorUiModel
import entity.Actor
import entity.Actor.Gender
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toUiState should map Actor to ActorUiModel correctly`() {
        val dummyActor = Actor(
            id = 1,
            name = "Sanaa",
            imageUrl = "https://example.com/image.jpg",
            region = null,
            lastShow = null,
            gender = Gender.MALE,
            department = null,
            character = null,
            birthDate = null,
            deathDate = null,
            placeOfBirth = null,
            biography = null
        )


        val result = dummyActor.toUiState()


        val expected = ActorUiModel(
            id = 1,
            name = "Sanaa",
            imageUrl = "https://example.com/image.jpg"
        )

        assertThat(result).isEqualTo(expected)
    }
}