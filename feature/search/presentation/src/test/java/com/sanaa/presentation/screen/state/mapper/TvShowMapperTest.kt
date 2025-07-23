package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.TvShowUiModel
import org.junit.jupiter.api.Test
import search.usecase.search_param.SearchTvSeriesOutput

class TvShowMapperTest {
    @Test
    fun `toUiState should map SearchTvSeriesOutput to TvShowUiModel correctly`() {
        // Arrange
        val tvSeries =
            createSearchTvSeriesOutput(101, "Breaking Bad", "https://example.com/breaking.jpg")

        // Act
        val result = tvSeries.toUiState()

        // Assert
        val expected =
            createExpectedUiModel(101, "Breaking Bad", "https://example.com/breaking.jpg")
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        fun createSearchTvSeriesOutput(
            id: Int,
            title: String,
            posterUrl: String
        ): SearchTvSeriesOutput {
            return SearchTvSeriesOutput(
                id = id,
                title = title,
                posterImageUrl = posterUrl
            )
        }

        fun createExpectedUiModel(id: Int, title: String, imageUrl: String): TvShowUiModel {
            return TvShowUiModel(
                id = id,
                title = title,
                imageUrl = imageUrl,
                rating = ""
            )
        }
    }
}