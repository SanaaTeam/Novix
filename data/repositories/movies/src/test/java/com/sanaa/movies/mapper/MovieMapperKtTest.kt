package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import entity.Genre
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieMapperKtTest{


    @Test
    fun `should map id correctly when mapping SimilarMoviesDtoResults to domain`() {
        val result = createSampleSimilarMovieDto().toDomain()
        assertThat(result.id).isEqualTo(7)
    }

    @Test
    fun `should map genres correctly when mapping SimilarMoviesDtoResults to domain`() {
        val result = createSampleSimilarMovieDto().toDomain()
        assertThat(result.genres).containsExactly(Genre.ACTION, Genre.ADVENTURE)
    }

    @Test
    fun `should map imdbRating correctly when mapping SimilarMoviesDtoResults to domain`() {
        val result = createSampleSimilarMovieDto().toDomain()
        assertThat(result.imdbRating).isEqualTo(7.7f)
    }

    @Test
    fun `should map id correctly when mapping MovieDetailsDto to domain`() {
        val result = createSampleMovieDetailsDto().toDomain()
        assertThat(result.id).isEqualTo(42)
    }

    @Test
    fun `should map posterImageUrl correctly when mapping MovieDetailsDto to domain`() {
        val result = createSampleMovieDetailsDto().toDomain()
        assertThat(result.posterImageUrl).contains("/poster.jpg")
    }

    @Test
    fun `should map title correctly when mapping MovieDetailsDto to domain`() {
        val result = createSampleMovieDetailsDto().toDomain()
        assertThat(result.title).isEqualTo("The Matrix")
    }

    @Test
    fun `should map genres correctly when mapping MovieDetailsDto to domain`() {
        val result = createSampleMovieDetailsDto().toDomain()
        assertThat(result.genres).contains(Genre.ACTION)
    }
    @Test
    fun `toDomain returns default title when null`() {
        val dto = MovieDetailsDto(id = 1, title = null)
        assertThat(dto.toDomain().title).isEqualTo("Unknown Title")
    }

    @Test
    fun `toDomain returns default overview when null`() {
        val dto = MovieDetailsDto(id = 1, overview = null)
        assertThat(dto.toDomain().overview).isEqualTo("No overview available")
    }

    @Test
    fun `toDomain returns default releaseDate when null`() {
        val dto = MovieDetailsDto(id = 1, releaseDate = null)
        assertThat(dto.toDomain().releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `toDomain returns default voteAverage when null`() {
        val dto = MovieDetailsDto(id = 1, voteAverage = null)
        assertThat(dto.toDomain().imdbRating).isEqualTo(0.0f)
    }

    @Test
    fun `toDomain returns default duration when null`() {
        val dto = MovieDetailsDto(id = 1, duration = null)
        assertThat(dto.toDomain().duration).isEqualTo(0)
    }

    @Test
    fun `toDomain maps valid genre correctly`() {
        val dto = MovieDetailsDto(id = 1, genres = arrayListOf(MovieDetailsDto.Genres(id = 28)))
        assertThat(dto.toDomain().genres).containsExactly(Genre.ACTION)
    }

    @Test
    fun `toDomain ignores null genre id`() {
        val dto = MovieDetailsDto(id = 1, genres = arrayListOf(MovieDetailsDto.Genres(id = null)))
        assertThat(dto.toDomain().genres).isEmpty()
    }

    @Test
    fun `toDomain returns empty poster url when path is null`() {
        val dto = MovieDetailsDto(id = 1, posterImagePath = null)
        assertThat(dto.toDomain().posterImageUrl).isEmpty()
    }

    @Test
    fun `toDomain returns full poster url when path is valid`() {
        val dto = MovieDetailsDto(id = 1, posterImagePath = "/poster.jpg")
        assertThat(dto.toDomain().posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
    }


    private fun createSampleMovieDetailsDto() = MovieDetailsDto(
        id = 42,
        posterImagePath = "/poster.jpg",
        title = "The Matrix",
        genres = arrayListOf(MovieDetailsDto.Genres(id = 28)),
        voteAverage = 8.5f,
        duration = 120,
        releaseDate = "1999-03-31",
        overview = "Sci-fi classic",
        adult = false,
        backdropPath = "/backdrop.jpg",
        budget = 63000000,
        homepage = "https://thematrix.com",
        imdbId = "tt0133093",
        originCountry = arrayListOf("US"),
        originalLanguage = "en",
        originalTitle = "The Matrix",
        popularity = 88.0,
        revenue = 463517383,
        status = "Released",
        tagline = "Welcome to the Real World.",
        video = false,
        voteCount = 22000
    )
    private fun createSampleSimilarMovieDto() = SimilarMoviesDto.Results(
        id = 7,
        posterPath = "/similar.jpg",
        title = "Similar Movie",
        genreIds = arrayListOf(28, 12),
        voteAverage = 7.7,
        releaseDate = "2020-01-01",
        overview = "Another good movie",
        adult = false,
        backdropPath = "/backdrop_similar.jpg",
        originalLanguage = "en",
        originalTitle = "Similar Movie Original",
        popularity = 70.0,
        video = false,
        voteCount = 1000
    )
}