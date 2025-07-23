package com.sanaa.presentation.screen.state.mapper

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.MovieUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import search.usecase.search_param.SearchMovieOutput

class MovieMapperTest {


    @Test
    fun `toActorUiState should map SearchMovieOutput to MovieUiModel correctly`() {
        // Arrange
        val movieOutput =
            createSearchMovieOutput(101, "The Matrix", "https://example.com/poster.jpg")

        // Act
        val result = movieOutput.toUiState()

        // Assert
        val expected =
            createExpectedMovieUiModel(101, "The Matrix", "https://example.com/poster.jpg")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `List toActorUiState should map list of SearchMovieOutput to list of MovieUiModel`() {
        // Arrange
        val list = listOf(
            createSearchMovieOutput(1, "Movie A", "url1"),
            createSearchMovieOutput(2, "Movie B", "url2")
        )

        // Act
        val result = list.toUiState()

        // Assert
        val expected = listOf(
            createExpectedMovieUiModel(1, "Movie A", "url1"),
            createExpectedMovieUiModel(2, "Movie B", "url2")
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Flow toActorUiState should map flow of SearchMovieOutput to MovieUiModel`() = runTest {
        // Arrange
        val flow = flowOf(
            listOf(
                createSearchMovieOutput(10, "Movie X", "posterX"),
                createSearchMovieOutput(20, "Movie Y", "posterY")
            )
        )

        val expected = listOf(
            createExpectedMovieUiModel(10, "Movie X", "posterX"),
            createExpectedMovieUiModel(20, "Movie Y", "posterY")
        )

        // Act & Assert
        flow.toUiState().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(expected)
            awaitComplete()
        }
    }

    companion object {
        fun createSearchMovieOutput(id: Int, title: String, posterUrl: String) =
            SearchMovieOutput(id = id, title = title, posterImageUrl = posterUrl)

        fun createExpectedMovieUiModel(id: Int, title: String, imageUrl: String) =
            MovieUiModel(id = id, title = title, imageUrl = imageUrl, rating = "")
    }
}
