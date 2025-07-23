package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.ActorUiModel
import org.junit.jupiter.api.Test
import search.usecase.search_param.SearchActorOutput

class ActorMapperTest {

    @Test
    fun `toUiState should map SearchActorOutput to ActorUiModel correctly`() {
        // Arrange
        val searchActorOutput = SearchActorOutput(
            id = 1,
            name = "Sanaa",
            profileImageUrl = "https://example.com/image.jpg"
        )

        // Act
        val result = searchActorOutput.toUiState()

        // Assert
        val expected = ActorUiModel(
            id = 1,
            name = "Sanaa",
            imageUrl = "https://example.com/image.jpg"
        )

        assertThat(result).isEqualTo(expected)
    }
}