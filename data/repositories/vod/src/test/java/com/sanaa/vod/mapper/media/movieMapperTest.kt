package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import entity.Genre
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes

class MovieMapperTest {

    @Test
    fun `toDomain maps MovieDto to Movie correctly with full data`() {
        val dto = MovieDto(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            posterImagePath = "/poster.jpg",
            voteAverage = 8.8f,
            duration = 148,
            releaseDate = "2010-07-16",
            genres = listOf(GenreDto(28, "Action"), GenreDto(12, "Adventure"))
        )

        val movie = dto.toDomain()

        assertEquals(1, movie.id)
        assertEquals("Inception", movie.title)
        assertEquals("A mind-bending thriller", movie.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", movie.posterImageUrl)
        assertEquals(8.8f, movie.imdbRating)
        assertEquals(148.minutes, movie.duration)
        assertEquals(listOf(Genre(28, "Action"), Genre(12, "Adventure")), movie.genres)
        assertEquals(LocalDate(2010, 7, 16), movie.releaseDate)
    }

    @Test
    fun `toDomain handles nulls and defaults`() {
        val dto = MovieDto(
            id = 2,
            title = null,
            overview = "No data movie",
            posterImagePath = null,
            voteAverage = null,
            duration = null,
            releaseDate = null,
            genres = null
        )

        val movie = dto.toDomain()

        assertEquals(2, movie.id)
        assertEquals("", movie.title)
        assertEquals("No data movie", movie.overview)
        assertEquals("", movie.posterImageUrl)
        assertEquals(0f, movie.imdbRating)
        assertEquals(0.minutes, movie.duration)
        assertEquals(LocalDate(1900, 1, 1), movie.releaseDate)
        assertEquals(emptyList(), movie.genres)
    }
}