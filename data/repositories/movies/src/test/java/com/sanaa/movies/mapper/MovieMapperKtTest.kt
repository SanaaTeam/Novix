package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import entity.Genre
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
        belongsToCollection = null,
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