package com.sanaa.vod.repository.mapper.media


import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import entity.Genre
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TvShowMapperTest {

    @Test
    fun `toEntity maps full TvShowDto correctly`() {
        val dto = TvShowDto(
            id = 1,
            name = "Breaking Bad",
            overview = "High school chemistry teacher turned meth kingpin.",
            posterPath = "/test.jpg",
            voteAverage = 9.5f,
            firstAirDate = "2008-01-20",
            genres = listOf(GenreDto(18, "Drama"), GenreDto(80, "Crime")),
            seasonsCount = 5
        )

        val result = dto.toEntity()

        assertEquals(1, result.id)
        assertEquals("Breaking Bad", result.title)
        assertEquals("High school chemistry teacher turned meth kingpin.", result.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/breakingbad.jpg", result.posterImageUrl)
        assertEquals(9.5f, result.imdbRating)
        assertEquals(LocalDate(2008, 1, 20), result.releaseDate)
        assertEquals(
            listOf(Genre(id = 18, name = "Drama"), Genre(id = 80, name = "Crime")),
            result.genres
        )
        assertEquals(5, result.seasonsCount)
    }

    @Test
    fun `toEntity throws exception on invalid date`() {
        val dto = TvShowDto(
            id = 3,
            name = "Invalid Show",
            overview = "This show has a bad date.",
            posterPath = "/invalid.jpg",
            voteAverage = 5.0f,
            firstAirDate = "invalid-date",
            genres = listOf(),
            seasonsCount = 1
        )

        assertFailsWith<IllegalArgumentException> {
            dto.toEntity()
        }
    }
}