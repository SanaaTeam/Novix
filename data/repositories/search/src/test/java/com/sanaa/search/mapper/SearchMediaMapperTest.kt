package com.sanaa.search.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import org.junit.jupiter.api.Test

class SearchMediaMapperTest {


    @Test
    fun `given MovieSearchDto when toLocalDto called should map id correctly`() {
        val dto = createMovieSearchDto(id = 1)
        val result = dto.toLocalDto(language = "en")
        assertThat(result.id).isEqualTo(1)
    }

    @Test
    fun `given MovieSearchDto when toLocalDto called should map title correctly`() {
        val dto = createMovieSearchDto(title = "Inception")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.title).isEqualTo("Inception")
    }

    @Test
    fun `given MovieSearchDto when toLocalDto called should map imagePath correctly`() {
        val dto = createMovieSearchDto(posterImagePath = "/inception.jpg")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.imagePath).isEqualTo("https://image.tmdb.org/t/p/w500/inception.jpg")
    }

    @Test
    fun `given MovieSearchDto when toLocalDto called should map language correctly`() {
        val dto = createMovieSearchDto()
        val result = dto.toLocalDto(language = "en")
        assertThat(result.language).isEqualTo("en")
    }

    @Test
    fun `given MovieSearchDto with releaseDate when toLocalDto called should extract releaseYear correctly`() {
        val dto = createMovieSearchDto(releaseDate = "2010-07-16")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.releaseYear).isEqualTo(2010)
    }

    @Test
    fun `given MovieSearchDto with genreIds when toLocalDto called should map genres correctly`() {
        val dto = createMovieSearchDto(genreIds = listOf(28, 878))
        val result = dto.toLocalDto(language = "en")
        assertThat(result.genres).isEqualTo("28, 878")
    }

    @Test
    fun `given MovieSearchDto with voteAverage when toLocalDto called should map imdbRating correctly`() {
        val dto = createMovieSearchDto(voteAverage = 8.8f)
        val result = dto.toLocalDto(language = "en")
        assertThat(result.imdbRating).isEqualTo(8.8f)
    }

    private fun createMovieSearchDto(
        id: Int = 1,
        title: String? = null,
        posterImagePath: String? = null,
        releaseDate: String? = null,
        genreIds: List<Int>? = null,
        voteAverage: Float? = null
    ) = MovieSearchDto(
        id = id,
        title = title,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate,
        genreIds = genreIds,
        voteAverage = voteAverage
    )

    private fun createMovieLocalDto(
        id: Int = 1,
        title: String = "",
        imagePath: String? = null,
        language: String = "en",
        releaseYear: Int? = null,
        genres: String? = null,
        imdbRating: Float? = null
    ) = MovieLocalDto(
        id = id,
        title = title,
        imagePath = imagePath,
        language = language,
        releaseYear = releaseYear,
        genres = genres,
        imdbRating = imdbRating
    )

    private fun createTvShowSearchDto(
        id: Int = 1,
        name: String? = null,
        posterImagePath: String? = null,
        releaseDate: String? = null,
        genreIds: List<Int>? = null,
        voteAverage: Float? = null
    ) = TvShowSearchDto(
        id = id,
        name = name,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate,
        genreIds = genreIds,
        voteAverage = voteAverage
    )

    private fun createTvSeriesLocalDto(
        id: Int = 1,
        title: String = "",
        imagePath: String? = null,
        language: String = "en",
        releaseYear: Int? = null,
        genres: String? = null,
        imdbRating: Float? = null
    ) = TvSeriesLocalDto(
        id = id,
        title = title,
        imagePath = imagePath,
        language = language,
        releaseYear = releaseYear,
        genres = genres,
        imdbRating = imdbRating
    )
}