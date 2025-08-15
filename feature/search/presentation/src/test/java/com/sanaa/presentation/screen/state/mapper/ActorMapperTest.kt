package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.ActorUiModel
import entity.Actor
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toUiState should map Actor to ActorUiModel correctly`() {
        val dummyActor = Actor(
            id = 1,
            name = "Sanaa",
            imageUrl = "https://example.com/image.jpg",
            department = "",
            character = "",
            birthDate = LocalDate(1, 1, 1),
            deathDate = LocalDate(1, 1, 1),
            placeOfBirth = "",
            biography = ""
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