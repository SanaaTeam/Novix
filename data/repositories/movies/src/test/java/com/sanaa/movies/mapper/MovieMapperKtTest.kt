package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.MovieDto
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
        val dto = MovieDto(id = 1, title = null)
        assertThat(dto.toDomain().title).isEqualTo("Unknown Title")
    }

    @Test
    fun `toDomain returns default overview when null`() {
        val dto = MovieDto(id = 1, overview = null)
        assertThat(dto.toDomain().overview).isEqualTo("No overview available")
    }

    @Test
    fun `toDomain returns default releaseDate when null`() {
        val dto = MovieDto(id = 1, releaseDate = null)
        assertThat(dto.toDomain().releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `toDomain returns default voteAverage when null`() {
        val dto = MovieDto(id = 1, voteAverage = null)
        assertThat(dto.toDomain().imdbRating).isEqualTo(0.0f)
    }

    @Test
    fun `toDomain returns default duration when null`() {
        val dto = MovieDto(id = 1, duration = null)
        assertThat(dto.toDomain().duration).isEqualTo(0)
    }

    @Test
    fun `toDomain maps valid genre correctly`() {
        val dto = MovieDto(id = 1, genres = arrayListOf(MovieDto.Genres(id = 28)))
        assertThat(dto.toDomain().genres).containsExactly(Genre.ACTION)
    }

    @Test
    fun `toDomain ignores null genre id`() {
        val dto = MovieDto(id = 1, genres = arrayListOf(MovieDto.Genres(id = null)))
        assertThat(dto.toDomain().genres).isEmpty()
    }

    @Test
    fun `toDomain returns empty poster url when path is null`() {
        val dto = MovieDto(id = 1, posterImagePath = null)
        assertThat(dto.toDomain().posterImageUrl).isEmpty()
    }

    @Test
    fun `toDomain returns full poster url when path is valid`() {
        val dto = MovieDto(id = 1, posterImagePath = "/poster.jpg")
        assertThat(dto.toDomain().posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
    }

    @Test
    fun `should map id correctly when mapping MoviesByCategoryDto to domain`() {
        val dto = createCategoryMovieDto(id = 99)
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo(99)
    }

    @Test
    fun `should map title correctly when mapping MoviesByCategoryDto to domain`() {
        val dto = createCategoryMovieDto(title = "Category Movie")
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Category Movie")
    }

    @Test
    fun `should fallback to empty title when mapping MoviesByCategoryDto with null title`() {
        val dto = createCategoryMovieDto(title = null)
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Unknown Title")
    }

    @Test
    fun `should map poster url correctly when mapping MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(posterPath = "/cat.jpg")
        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/cat.jpg")
    }

    @Test
    fun `should return empty poster url when MoviesByCategoryDto poster path is null`() {
        val dto = createCategoryMovieDto(posterPath = null)
        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEmpty()
    }

    @Test
    fun `should map release date correctly when provided in MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(releaseDate = "2024-02-10")
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(2024, 2, 10))
    }

    @Test
    fun `should fallback to default date when release date is null in MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(releaseDate = null)
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `should map genres correctly when mapping MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(genreIds = listOf(28, 12))
        val result = dto.toDomain()
        assertThat(result.genres).containsExactly(Genre.ACTION, Genre.ADVENTURE)
    }

    @Test
    fun `should ignore invalid genre ids when mapping MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(genreIds = listOf(999))
        val result = dto.toDomain()
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `should fallback to empty overview when overview is null in MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(overview = null)
        val result = dto.toDomain()
        assertThat(result.overview).isEqualTo("")
    }

    @Test
    fun `should map overview correctly when mapping MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(overview = "A great movie")
        val result = dto.toDomain()
        assertThat(result.overview).isEqualTo("A great movie")
    }

    @Test
    fun `should map voteAverage correctly when mapping MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(voteAverage = 6.9)
        val result = dto.toDomain()
        assertThat(result.imdbRating).isEqualTo(6.9f)
    }

    @Test
    fun `should fallback to 0 rating when voteAverage is null in MoviesByCategoryDto`() {
        val dto = createCategoryMovieDto(voteAverage = null)
        val result = dto.toDomain()
        assertThat(result.imdbRating).isEqualTo(0.0f)
    }


    @Test
    fun `should return full image url when path is valid`() {
        val result = getFullImageUrl("/path.jpg")
        assertThat(result).isEqualTo("https://image.tmdb.org/t/p/w500/path.jpg")
    }

    @Test
    fun `should return empty string when path is null`() {
        val path: String? = null
        val result = getFullImageUrl(path)
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return empty string when path is blank`() {
        val result = getFullImageUrl("   ")
        assertThat(result).isEqualTo("")
    }


    private fun createSampleMovieDetailsDto() = MovieDto(
        id = 42,
        posterImagePath = "/poster.jpg",
        title = "The Matrix",
        genres = arrayListOf(MovieDto.Genres(id = 28)),
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
    private fun createCategoryMovieDto(
        id: Int = 10,
        title: String? = "Default Title",
        posterPath: String? = "/default.jpg",
        releaseDate: String? = "2023-01-01",
        genreIds: List<Int> = listOf(28),
        overview: String? = "Default overview",
        voteAverage: Double? = 5.5
    ) = MoviesByCategoryResponse.MoviesByCategoryDto(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        genreIds = ArrayList(genreIds),
        overview = overview,
        voteAverage = voteAverage,
    )


}