package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class ActorMapperTest {

    @Test
    fun `toDomain maps ActorDto to Actor correctly`() {
        val dto = ActorDto(
            id = 1,
            profileImagePath = "/image.jpg",
            name = "John Doe",
            knownForDepartment = "Acting",
            gender = 1,
            birthDay = "1980-05-10",
            deathDay = null,
            placeOfBirth = "USA",
            biography = "Some bio"
        )

        val result = dto.toEntity()

        assertEquals(1, result.id)
        assertEquals("https://image.tmdb.org/t/p/w500/image.jpg", result.imageUrl)
        assertEquals("John Doe", result.name)
        assertEquals("Acting", result.department)
        assertEquals(LocalDate(1980, 5, 10), result.birthDate)
        assertEquals(getLocalDateOrDefault(null), result.deathDate)
        assertEquals("USA", result.placeOfBirth)
        assertEquals("Some bio", result.biography)
    }

    @Test
    fun `toMovie maps ActorCastCreditDto to Movie correctly`() {
        val dto = ActorCastCreditDto(
            id = 100,
            posterPath = "/poster.jpg",
            movieTitle = "Example Movie",
            tvShowTitle = null,
            voteAverage = 7.5,
            releaseDate = "2021-01-01",
            firstAirDate = null,
            overview = "A great movie"
        )

        val result = dto.toMovie()

        assertEquals(100, result.id)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterImageUrl)
        assertEquals("Example Movie", result.title)
        assertEquals(7.5f, result.imdbRating)
        assertEquals(LocalDate(2021, 1, 1), result.releaseDate)
        assertEquals("A great movie", result.overview)
    }

    @Test
    fun `toTvShow maps ActorCastCreditDto to TvShow correctly`() {
        val dto = ActorCastCreditDto(
            id = 200,
            posterPath = "/poster.jpg",
            movieTitle = null,
            tvShowTitle = "Example Series",
            voteAverage = 8.3,
            releaseDate = null,
            firstAirDate = "2022-03-15",
            overview = "A great series"
        )

        val result = dto.toTvShow()

        assertEquals(200, result.id)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterImageUrl)
        assertEquals("Example Series", result.title)
        assertEquals(8.3f, result.imdbRating)
        assertEquals(LocalDate(2022, 3, 15), result.releaseDate)
        assertEquals("A great series", result.overview)
    }

    @Test
    fun `getFullImageUrl returns full url or empty string`() {
        assertEquals("https://image.tmdb.org/t/p/w500/image.jpg", getFullImageUrl("/image.jpg"))
        assertEquals("", getFullImageUrl(null))
        assertEquals("", getFullImageUrl(""))
    }
}