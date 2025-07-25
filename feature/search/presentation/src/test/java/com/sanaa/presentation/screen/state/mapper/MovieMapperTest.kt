package com.sanaa.presentation.screen.state.mapper

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.MovieUiModel
import entity.Movie
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieMapperTest {


    @Test
    fun `toActorUiState should map Movie to MovieUiModel correctly`() {
        val movieOutput =
            createMovie(101, "The Matrix", "https://example.com/poster.jpg")

        val result = movieOutput.toUiState()

        val expected =
            createExpectedMovieUiModel(101, "The Matrix", "https://example.com/poster.jpg")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `List toActorUiState should map list of Movie to list of MovieUiModel`() {
        val list = listOf(
            createMovie(1, "Movie A", "url1"),
            createMovie(2, "Movie B", "url2")
        )

        val result = list.toUiState()

        val expected = listOf(
            createExpectedMovieUiModel(1, "Movie A", "url1"),
            createExpectedMovieUiModel(2, "Movie B", "url2")
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Flow toActorUiState should map flow of Movie to MovieUiModel`() = runTest {
        val flow = flowOf(
            listOf(
                createMovie(10, "Movie X", "posterX"),
                createMovie(20, "Movie Y", "posterY")
            )
        )

        val expected = listOf(
            createExpectedMovieUiModel(10, "Movie X", "posterX"),
            createExpectedMovieUiModel(20, "Movie Y", "posterY")
        )

        flow.toUiState().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(expected)
            awaitComplete()
        }
    }

    companion object {
        fun createMovie(id: Int, title: String, posterUrl: String) =
            Movie(
                id = id, title = title, posterImageUrl = posterUrl, genres = emptyList(),
                imdbRating = 0f,
                duration = 1,
                releaseDate = LocalDate(1970, 1, 1),
                overview = "",
                trailerUrl = ""
            )

        fun createExpectedMovieUiModel(id: Int, title: String, imageUrl: String) =
            MovieUiModel(id = id, title = title, imageUrl = imageUrl, rating = "")
    }
}
