package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.TvShowUiModel
import org.junit.jupiter.api.Test

class TvShowMapperTest {
    @Test
    fun `toUiState should map SearchTvSeriesOutput to TvShowUiModel correctly`() {
        val tvSeries =
            createSearchTvSeriesOutput(101, "Breaking Bad", "https://example.com/breaking.jpg")

        val result = tvSeries.toUiState()

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