package com.sanaa.series

import com.google.common.truth.Truth.assertThat
import com.sanaa.series.dto.ActorDto
import com.sanaa.series.dto.AuthorDetailsDto
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.ReviewDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.TvSeriesImageDto
import com.sanaa.series.dto.TvSeriesVideoDto
import com.sanaa.series.mapper.apiGenderMapping
import com.sanaa.series.mapper.buildPosterUrl
import com.sanaa.series.mapper.getFullImageUrl
import com.sanaa.series.mapper.toDtoId
import com.sanaa.series.mapper.toEntity
import entity.Actor
import entity.Genre
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapperTest {


    @Test
    fun `should map title when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(name = "My Series").toEntity()
        assertThat(result.title).isEqualTo("My Series")
    }

    @Test
    fun `should map seasonsCount when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(seasonsCount = 3).toEntity()
        assertThat(result.seasonsCount).isEqualTo(3)
    }

    @Test
    fun `should map overview when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(overview = "Overview text").toEntity()
        assertThat(result.overview).isEqualTo("Overview text")
    }

    @Test
    fun `should map firstAirDate when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(firstAirDate = "2023-06-01").toEntity()
        assertEquals(LocalDate.parse("2023-06-01"), result.releaseDate)
    }

    @Test
    fun `should map voteAverage when TvSeriesDto is mapped`() {

        val result = createTvSeriesDto(voteAverage = 8.5f).toEntity()
        assertThat(result.imdbRating).isEqualTo(8.5f)
    }

    @Test
    fun `should map posterPath when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(posterPath = "/poster.jpg").toEntity()
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterImageUrl)
    }

    @Test
    fun `should map genres when TvSeriesDto is mapped`() {
        val result = createTvSeriesDto(
            genres = listOf(
                GenreDto(28, "Action"),
                GenreDto(35, "Comedy")
            )
        ).toEntity()
        assertTrue(result.genres.contains(Genre.ACTION))
        assertTrue(result.genres.contains(Genre.COMEDY))
    }

    private fun createTvSeriesDto(
        id: Int = 1,
        name: String = "My Series",
        overview: String = "Overview text",
        posterPath: String = "/poster.jpg",
        voteAverage: Float = 8.5f,
        firstAirDate: String = "2023-06-01",
        genres: List<GenreDto> = listOf(GenreDto(28, "Action"), GenreDto(35, "Comedy")),
        seasonsCount: Int = 3
    ) = TvSeriesDto(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        voteAverage = voteAverage,
        firstAirDate = firstAirDate,
        genres = genres,
        seasonsCount = seasonsCount
    )

}
