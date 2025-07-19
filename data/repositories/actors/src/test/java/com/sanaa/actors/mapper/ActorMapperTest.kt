package com.sanaa.actors.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.actors.dataSource.remote.dto.ActorDto
import com.sanaa.actors.dataSource.remote.dto.ActorMovieCastDto.MovieCastCreditDto
import com.sanaa.actors.dataSource.remote.dto.ActorTvCastDto.TvCastCreditDto
import entity.Actor
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toFullImageUrl appends base URL`() {
        val path = "/img.jpg"
        val fullUrl = getFullImageUrl(path)

        assertThat(fullUrl).isEqualTo("https://image.tmdb.org/t/p/w500/img.jpg")
    }

    @Test
    fun `ActorDto toDomain maps all fields correctly`() {
        val dto = ActorDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/img.jpg",
            biography = "Biography",
            birthDay = "1956-07-09",
            deathDay = "2020-01-01",
            gender = 2,
            knownForDepartment = "Acting",
            placeOfBirth = "USA"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Tom Hanks")
        assertThat(result.imageUrl).contains("/img.jpg")
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
        assertThat(result.region).isEqualTo(null)
        assertThat(result.biography).isEqualTo("Biography")
        assertThat(result.birthDate).isEqualTo(LocalDate(1956, 7, 9))
        assertThat(result.deathDate).isEqualTo(LocalDate(2020, 1, 1))
    }

    @Test
    fun `ActorDto toDomain uses fallback values`() {
        val dto = ActorDto(
            id = 99,
            name = null,
            profileImagePath = null,
            biography = null,
            gender = null,
            birthDay = null,
            deathDay = null,
            knownForDepartment = null,
            placeOfBirth = null
        )

        val result = dto.toDomain()

        assertThat(result.name).isEqualTo("Unknown")
        assertThat(result.imageUrl).isEqualTo("")
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
        assertThat(result.birthDate).isNull()
        assertThat(result.deathDate).isNull()
        assertThat(result.region).isEqualTo(null)
    }

    @Test
    fun `MovieCastCreditDto toDomain maps correctly`() {
        val dto = MovieCastCreditDto(
            movieId = 10,
            title = "Inception",
            originalTitle = "Inception (Original)",
            posterPath = "/poster.jpg",
            releaseDate = "2010-07-16",
            voteAverage = 8.8,
            overview = "Dreams within dreams"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(10)
        assertThat(result.title).isEqualTo("Inception")
        assertThat(result.posterImageUrl).contains("/poster.jpg")
        assertThat(result.imdbRating).isEqualTo(8.8f)
        assertThat(result.releaseDate).isEqualTo(LocalDate(2010, 7, 16))
        assertThat(result.overview).isEqualTo("Dreams within dreams")
    }

    @Test
    fun `MovieCastCreditDto toDomain uses fallback for missing data`() {
        val dto = MovieCastCreditDto(
            movieId = 99,
            title = null,
            originalTitle = null,
            posterPath = null,
            releaseDate = null,
            voteAverage = null,
            overview = null
        )

        val result = dto.toDomain()

        assertThat(result.title).isEqualTo("Unknown")
        assertThat(result.posterImageUrl).isEmpty()
        assertThat(result.imdbRating).isEqualTo(0f)
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `TvCastCreditDto toDomain maps correctly`() {
        val dto = TvCastCreditDto(
            tvId = 22,
            name = "Breaking Bad",
            originalName = "Breaking Bad (Original)",
            posterPath = "/tv.jpg",
            firstAirDate = "2008-01-20",
            voteAverage = 9.5,
            overview = "Chemistry teacher turns to crime"
        )

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(22)
        assertThat(result.title).isEqualTo("Breaking Bad")
        assertThat(result.posterImageUrl).contains("/tv.jpg")
        assertThat(result.imdbRating).isEqualTo(9.5f)
        assertThat(result.releaseDate).isEqualTo(LocalDate(2008, 1, 20))
    }

    @Test
    fun `TvCastCreditDto toDomain uses fallback for missing fields`() {
        val dto = TvCastCreditDto(
            tvId = 33,
            name = null,
            originalName = null,
            posterPath = null,
            firstAirDate = null,
            voteAverage = null,
            overview = null
        )

        val result = dto.toDomain()

        assertThat(result.title).isEqualTo("Unknown")
        assertThat(result.posterImageUrl).isEqualTo("")
        assertThat(result.imdbRating).isEqualTo(0f)
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
        assertThat(result.overview).isEqualTo("")
    }

    @Test
    fun `ActorDto maps FEMALE branch and ignores blank dates`() {
        val dto = ActorDto(
            id = 7,
            name = "Meryl Streep",
            profileImagePath = "/streep.jpg",
            biography = null,
            birthDay = "",
            deathDay = "",
            gender = 1,
            knownForDepartment = null,
            placeOfBirth = null
        )

        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
        assertThat(result.birthDate).isNull()
        assertThat(result.deathDate).isNull()
    }

    /* ---------- MovieCastCreditDto ---------- */

    @Test
    fun `MovieCastCreditDto uses originalTitle when title is null`() {
        val dto = MovieCastCreditDto(
            movieId = 55,
            title = null,
            originalTitle = "Le Fabuleux Destin d'Amélie Poulain",
            posterPath = null,
            releaseDate = null,
            voteAverage = null,
            overview = null
        )

        val r = dto.toDomain()
        assertThat(r.title)
            .isEqualTo("Le Fabuleux Destin d'Amélie Poulain")
    }

    @Test
    fun `TvCastCreditDto uses originalName when name is null`() {
        val dto = TvCastCreditDto(
            tvId = 77,
            name = null,
            originalName = "La Casa de Papel",
            posterPath = null,
            firstAirDate = null,
            voteAverage = null,
            overview = null
        )

        val r = dto.toDomain()
        assertThat(r.title).isEqualTo("La Casa de Papel")
    }


    @Test
    fun `ActorDto imageUrl falls back when helper returns blank`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>()) } returns ""

        val dto = ActorDto(
            id = 11,
            name = "Someone",
            profileImagePath = "/profile.jpg",
            biography = null,
            birthDay = null,
            deathDay = null,
            gender = 2,
            knownForDepartment = null,
            placeOfBirth = null
        )

        val result = dto.toDomain()
        assertThat(result.imageUrl).isEmpty()
    }

    @Test
    fun `MovieCastCreditDto posterImageUrl falls back when helper returns blank`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>())  } returns ""

        val dto = MovieCastCreditDto(
            movieId = 21, title = "Title", originalTitle = null,
            posterPath = "/poster.jpg", releaseDate = null,
            voteAverage = null, overview = null
        )

        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEmpty()
    }

    @Test
    fun `TvCastCreditDto posterImageUrl falls back when helper returns blank`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>()) } returns ""

        val dto = TvCastCreditDto(
            tvId = 31, name = "Show", originalName = null,
            posterPath = "/poster.jpg", firstAirDate = null,
            voteAverage = null, overview = null
        )

        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEmpty()
    }
    @Test
    fun `toLocalDateOrNull returns null on invalid format`() {
        val invalidDate = "2020-99-99"
        val result = toLocalDateOrNull(invalidDate)
        assertThat(result).isNull()
    }

    @Test
    fun `ActorDto maps unknown gender as MALE`() {
        val dto = ActorDto(
            id = 2,
            name = "Test",
            profileImagePath = null,
            biography = null,
            birthDay = null,
            deathDay = null,
            gender = 5, // unknown gender
            knownForDepartment = null,
            placeOfBirth = null
        )

        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should fallback to Unknown when MovieCastCreditDto title and originalTitle are blank`() {
        val dto = MovieCastCreditDto(
            movieId = 123,
            title = "",
            originalTitle = "",
            posterPath = null,
            releaseDate = null,
            voteAverage = null,
            overview = null
        )

        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("")
    }

    @Test
    fun `should fallback to Unknown when TvCastCreditDto name and originalName are blank`() {
        val dto = TvCastCreditDto(
            tvId = 321,
            name = "",
            originalName = "",
            posterPath = null,
            firstAirDate = null,
            voteAverage = null,
            overview = null
        )

        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("")
    }



    @AfterEach
    fun tearDown() = unmockkAll()
}