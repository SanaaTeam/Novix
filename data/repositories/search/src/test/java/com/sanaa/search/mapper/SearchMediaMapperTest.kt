package com.sanaa.search.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import org.junit.jupiter.api.Test

class SearchMediaMapperTest {

    // Movie
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


    @Test
    fun `given MovieSearchDto when toSearchOutput called should map id correctly`() {
        val dto = createMovieSearchDto(id = 2)
        val result = dto.toSearchOutput()
        assertThat(result.id).isEqualTo(2)
    }

    @Test
    fun `given MovieSearchDto when toSearchOutput called should map title correctly`() {
        val dto = createMovieSearchDto(title = "Interstellar")
        val result = dto.toSearchOutput()
        assertThat(result.title).isEqualTo("Interstellar")
    }

    @Test
    fun `given MovieSearchDto when toSearchOutput called should map posterImageUrl correctly`() {
        val dto = createMovieSearchDto(posterImagePath = "/interstellar.jpg")
        val result = dto.toSearchOutput()
        assertThat(result.posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/interstellar.jpg")
    }
    @Test
    fun `given MovieLocalDto when toSearchOutput called should map id correctly`() {
        val dto = createMovieLocalDto(id = 3)
        val result = dto.toSearchOutput()
        assertThat(result.id).isEqualTo(3)
    }

    @Test
    fun `given MovieLocalDto when toSearchOutput called should map title correctly`() {
        val dto = createMovieLocalDto(title = "Tenet")
        val result = dto.toSearchOutput()
        assertThat(result.title).isEqualTo("Tenet")
    }

    @Test
    fun `given MovieLocalDto when toSearchOutput called should map posterImageUrl correctly`() {
        val dto = createMovieLocalDto(imagePath = "/tenet.jpg")
        val result = dto.toSearchOutput()
        assertThat(result.posterImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/tenet.jpg")
    }

//TvShowSearchDto.toLocalDto

    @Test
    fun `given TvShowSearchDto when toLocalDto called should map id correctly`() {
        val dto = createTvShowSearchDto(id = 4)
        val result = dto.toLocalDto(language = "en")
        assertThat(result.id).isEqualTo(4)
    }

    @Test
    fun `given TvShowSearchDto when toLocalDto called should map title correctly`() {
        val dto = createTvShowSearchDto(name = "Breaking Bad")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.title).isEqualTo("Breaking Bad")
    }

    @Test
    fun `given TvShowSearchDto when toLocalDto called should map imagePath correctly`() {
        val dto = createTvShowSearchDto(posterImagePath = "/breaking.jpg")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.imagePath).isEqualTo("https://image.tmdb.org/t/p/w500/breaking.jpg")
    }

    @Test
    fun `given TvShowSearchDto when toLocalDto called should map language correctly`() {
        val dto = createTvShowSearchDto()
        val result = dto.toLocalDto(language = "en")
        assertThat(result.language).isEqualTo("en")
    }

    @Test
    fun `given TvShowSearchDto with releaseDate when toLocalDto called should extract releaseYear correctly`() {
        val dto = createTvShowSearchDto(releaseDate = "2008-01-20")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.releaseYear).isEqualTo(2008)
    }

    @Test
    fun `given TvShowSearchDto with genreIds when toLocalDto called should map genres correctly`() {
        val dto = createTvShowSearchDto(genreIds = listOf(80, 18))
        val result = dto.toLocalDto(language = "en")
        assertThat(result.genres).isEqualTo("80, 18")
    }

    @Test
    fun `given TvShowSearchDto with voteAverage when toLocalDto called should map imdbRating correctly`() {
        val dto = createTvShowSearchDto(voteAverage = 9.5f)
        val result = dto.toLocalDto(language = "en")
        assertThat(result.imdbRating).isEqualTo(9.5f)
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