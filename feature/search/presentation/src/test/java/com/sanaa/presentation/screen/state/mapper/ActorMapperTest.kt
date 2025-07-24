package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.ActorUiModel
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toUiState should map SearchActorOutput to ActorUiModel correctly`() {
        val searchActorOutput = SearchActorOutput(
            id = 1,
            name = "Sanaa",
            profileImageUrl = "https://example.com/image.jpg"
        )


        val result = searchActorOutput.toUiState()


        val expected = ActorUiModel(
            id = 1,
            name = "Sanaa",
            imageUrl = "https://example.com/image.jpg"
        )

        assertThat(result).isEqualTo(expected)
    }
}