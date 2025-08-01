package com.sanaa.presentation.screen.state.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.TvShowUiModel
import entity.TvSeries
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {
    @Test
    fun `toUiState should map TvSeries to TvShowUiModel correctly`() {
        val tvSeries =
            createTvSeries(101, "Breaking Bad", "https://example.com/breaking.jpg")

        val result = tvSeries.toUiState()

        val expected =
            createExpectedUiModel(101, "Breaking Bad", "https://example.com/breaking.jpg")
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        fun createTvSeries(
            id: Int,
            title: String,
            posterUrl: String
        ): TvSeries {
            return TvSeries(
                id = id,
                title = title,
                posterImageUrl = posterUrl,
                releaseDate = LocalDate(1970, 1, 1),
                genres = emptyList(),
                imdbRating = 10f,
                overview = "",
                seasonsCount = 0,
                rating = 0
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