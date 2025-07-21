package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieDto.GenreDto
import entity.Genre
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieMapperTest {

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = MovieDto(
            id = 42,
            posterImagePath = "/sample.jpg",
            title = "Inception",
            genres = listOf(GenreDto(id = 28)),
            voteAverage = 8.5f,
            duration = 148,
            releaseDate = "2010-07-16",
            overview = "A thief who steals corporate secrets..."
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(42)
        assertThat(result.posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/sample.jpg")
        assertThat(result.title).isEqualTo("Inception")
        assertThat(result.genres).containsExactly(Genre.ACTION)
        assertThat(result.imdbRating).isEqualTo(8.5f)
        assertThat(result.duration).isEqualTo(148)
        assertThat(result.releaseDate).isEqualTo(LocalDate(2010, 7, 16))
        assertThat(result.overview).isEqualTo("A thief who steals corporate secrets...")
    }

    @Test
    fun `toDomain maps null fields with default values`() {
        val dto = MovieDto(
            id = 1,
            posterImagePath = null,
            title = null,
            genres = null,
            voteAverage = null,
            duration = null,
            releaseDate = null,
            overview = null
        )

        val result = dto.toDomain()

        assertThat(result.posterImageUrl).isEqualTo("")
        assertThat(result.title).isEqualTo("")
        assertThat(result.genres).isEmpty()
        assertThat(result.imdbRating).isEqualTo(0.0f)
        assertThat(result.duration).isEqualTo(0)
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
        assertThat(result.overview).isNull()
    }
}