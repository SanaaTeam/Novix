package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import entity.Genre
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieMapperKtTest{
    @Test
    fun `MovieDetailsDto toDomain maps correctly`() {
        val dto = MovieDetailsDto(
            id = 42,
            posterImagePath = "/poster.jpg",
            title = "The Matrix",
            genres = arrayListOf(MovieDetailsDto.Genres(id = 28)),
            voteAverage = 8.5f,
            duration = 120,
            releaseDate = "1999-03-31",
            overview = "Sci-fi classic"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(42)
        assertThat(result.posterImageUrl).contains("/poster.jpg")
        assertThat(result.title).isEqualTo("The Matrix")
        assertThat(result.genres).contains(Genre.ACTION)
        assertThat(result.releaseDate).isEqualTo(LocalDate(1999, 3, 31))
    }

    @Test
    fun `SimilarMoviesDtoResults toDomain maps correctly`() {
        val dto = SimilarMoviesDto.Results(
            id = 7,
            posterPath = "/similar.jpg",
            title = "Similar Movie",
            genreIds = arrayListOf(28, 12),
            voteAverage = 7.7,
            releaseDate = "2020-01-01",
            overview = "Another good movie"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(7)
        assertThat(result.genres).containsExactly(Genre.ACTION, Genre.ADVENTURE)
        assertThat(result.imdbRating).isEqualTo(7.7f)
    }
}