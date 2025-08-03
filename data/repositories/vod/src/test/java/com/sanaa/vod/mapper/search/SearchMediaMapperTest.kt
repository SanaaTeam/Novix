package com.sanaa.vod.mapper.search

import com.google.common.truth.Truth
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import org.junit.jupiter.api.Test

class SearchMediaMapperTest {

    @Test
    fun `given MovieSearchDto when toSearchOutput called should map id correctly`() {
        val dto = createMovieSearchDto(id = 2)
        val result = dto.toEntity()
        Truth.assertThat(result.id).isEqualTo(2)
    }

    @Test
    fun `given MovieSearchDto when toSearchOutput called should map title correctly`() {
        val dto = createMovieSearchDto(title = "Interstellar")
        val result = dto.toEntity()
        Truth.assertThat(result.title).isEqualTo("Interstellar")
    }

    @Test
    fun `given MovieSearchDto when toSearchOutput called should map posterImageUrl correctly`() {
        val dto = createMovieSearchDto(posterImagePath = "/interstellar.jpg")
        val result = dto.toEntity()
        Truth.assertThat(result.posterImageUrl)
            .isEqualTo("https://image.tmdb.org/t/p/w500/interstellar.jpg")
    }

    @Test
    fun `given TvShowSearchDto when toSearchOutput called should map id correctly`() {
        val dto = createTvShowSearchDto(id = 5)
        val result = dto.toEntity()
        Truth.assertThat(result.id).isEqualTo(5)
    }

    @Test
    fun `given TvShowSearchDto when toSearchOutput called should map title correctly`() {
        val dto = createTvShowSearchDto(name = "Loki")
        val result = dto.toEntity()
        Truth.assertThat(result.title).isEqualTo("Loki")
    }

    @Test
    fun `given TvShowSearchDto when toSearchOutput called should map posterImageUrl correctly`() {
        val dto = createTvShowSearchDto(posterImagePath = "/loki.jpg")
        val result = dto.toEntity()
        Truth.assertThat(result.posterImageUrl)
            .isEqualTo("https://image.tmdb.org/t/p/w500/loki.jpg")
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
}