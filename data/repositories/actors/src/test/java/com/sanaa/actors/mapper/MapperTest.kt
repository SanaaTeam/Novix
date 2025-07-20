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

class MapperTest {


    @Test
    fun `should append base URL in toFullImageUrl`() {
        val path = "/img.jpg"
        val fullUrl = getFullImageUrl(path)
        assertThat(fullUrl).isEqualTo("https://image.tmdb.org/t/p/w500/img.jpg")
    }

    @Test
    fun `should map actor id correctly`() {
        val dto = createActorDto(id = 1)
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo(1)
    }

    @Test
    fun `should map actor name correctly`() {
        val dto = createActorDto(name = "Tom Hanks")
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Tom Hanks")
    }

    @Test
    fun `should return Unknown name when name is null`() {
        val dto = createActorDto(name = null)
        val result = dto.toDomain()
        assertThat(result.name).isEqualTo("Unknown name")
    }

    @Test
    fun `should map profile image URL correctly`() {
        val dto = createActorDto(profileImagePath = "/img.jpg")
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/img.jpg")
    }

    @Test
    fun `should return empty imageUrl when profileImagePath is null`() {
        val dto = createActorDto(profileImagePath = null)
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEqualTo("")
    }

    @Test
    fun `should map gender as MALE when gender is 2`() {
        val dto = createActorDto(gender = 2)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map gender as FEMALE when gender is 1`() {
        val dto = createActorDto(gender = 1)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should fallback to MALE when gender is unknown`() {
        val dto = createActorDto(gender = 5)
        val result = dto.toDomain()
        assertThat(result.gender).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should map biography when provided`() {
        val dto = createActorDto(biography = "Famous actor")
        val result = dto.toDomain()
        assertThat(result.biography).isEqualTo("Famous actor")
    }

    @Test
    fun `should fallback to empty biography when null`() {
        val dto = createActorDto(biography = null)
        val result = dto.toDomain()
        assertThat(result.biography).isEqualTo("")
    }

    @Test
    fun `should parse valid birth date`() {
        val dto = createActorDto(birthDay = "1980-01-01")
        val result = dto.toDomain()
        assertThat(result.birthDate).isEqualTo(LocalDate(1980, 1, 1))
    }

    @Test
    fun `should return null birthDate when blank`() {
        val dto = createActorDto(birthDay = "")
        val result = dto.toDomain()
        assertThat(result.birthDate).isNull()
    }

    @Test
    fun `should return null deathDate when blank`() {
        val dto = createActorDto(deathDay = "")
        val result = dto.toDomain()
        assertThat(result.deathDate).isNull()
    }

    @Test
    fun `should return empty imageUrl when helper returns blank`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>()) } returns ""

        val dto = createActorDto(profileImagePath = "/profile.jpg")
        val result = dto.toDomain()
        assertThat(result.imageUrl).isEmpty()
    }

    @Test
    fun `should map knownForDepartment when provided`() {
        val dto = createActorDto(knownForDepartment = "Acting")
        val result = dto.toDomain()
        assertThat(result.department).isEqualTo("Acting")
    }

    @Test
    fun `should map placeOfBirth when provided`() {
        val dto = createActorDto(placeOfBirth = "New York")
        val result = dto.toDomain()
        assertThat(result.placeOfBirth).isEqualTo("New York")
    }


    // MovieCastCreditDto

    @Test
    fun `should map movie id correctly`() {
        val dto = createMovieCastDto(movieId = 10)
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo(10)
    }

    @Test
    fun `should fallback to originalTitle when title is null`() {
        val dto = createMovieCastDto(title = null, originalTitle = "Fallback Title")
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Fallback Title")
    }

    @Test
    fun `should fallback to Unknown when title and originalTitle are null`() {
        val dto = createMovieCastDto(title = null, originalTitle = null)
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Unknown name")
    }

    @Test
    fun `should return 0f when voteAverage is null`() {
        val dto = createMovieCastDto(voteAverage = null)
        val result = dto.toDomain()
        assertThat(result.imdbRating).isEqualTo(0f)
    }

    @Test
    fun `should return empty posterImageUrl when helper returns blank in MovieCast`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>()) } returns ""

        val dto = createMovieCastDto(posterPath = "/poster.jpg")
        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEmpty()
    }

    @Test
    fun `should parse valid releaseDate correctly in MovieCast`() {
        val dto = createMovieCastDto(releaseDate = "2001-12-19")
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(2001, 12, 19))
    }

    @Test
    fun `should fallback to default releaseDate when null in MovieCast`() {
        val dto = createMovieCastDto(releaseDate = null)
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `should fallback to empty overview when null in MovieCast`() {
        val dto = createMovieCastDto(overview = null)
        val result = dto.toDomain()
        assertThat(result.overview).isEqualTo("")
    }

    // TvCastCreditDto

    @Test
    fun `should use name when available`() {
        val dto = createTvCastDto(name = "Breaking Bad")
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Breaking Bad")
    }

    @Test
    fun `should fallback to originalName when name is null`() {
        val dto = createTvCastDto(name = null, originalName = "Original")
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Original")
    }

    @Test
    fun `should fallback to Unknown when both name and originalName are null`() {
        val dto = createTvCastDto(name = null, originalName = null)
        val result = dto.toDomain()
        assertThat(result.title).isEqualTo("Unknown name")
    }

    @Test
    fun `should return empty posterImageUrl when helper returns blank in TvCastDto`() {
        mockkStatic("com.sanaa.actors.mapper.MapperKt")
        every { getFullImageUrl(any<String>()) } returns ""

        val dto = createTvCastDto(posterPath = "/poster.jpg")
        val result = dto.toDomain()
        assertThat(result.posterImageUrl).isEmpty()
    }

    @Test
    fun `should parse valid firstAirDate in TvCast`() {
        val dto = createTvCastDto(firstAirDate = "2015-05-01")
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(2015, 5, 1))
    }

    @Test
    fun `should fallback to default releaseDate when null in TvCast`() {
        val dto = createTvCastDto(firstAirDate = null)
        val result = dto.toDomain()
        assertThat(result.releaseDate).isEqualTo(LocalDate(1900, 1, 1))
    }

    @Test
    fun `should fallback to empty overview when null in TvCast`() {
        val dto = createTvCastDto(overview = null)
        val result = dto.toDomain()
        assertThat(result.overview).isEqualTo("")
    }


    @Test
    fun `should return null when parsing invalid date format`() {
        val invalidDate = "2020-99-99"
        val result = toLocalDateOrNull(invalidDate)
        assertThat(result).isNull()
    }

    @Test
    fun `toLocalDateOrNull returns null on invalid format`() {
        val invalidDate = "2020-99-99"
        val result = toLocalDateOrNull(invalidDate)
        assertThat(result).isNull()
    }


    @AfterEach
    fun tearDown() = unmockkAll()

    private fun createActorDto(
        id: Int = 1,
        name: String? = null,
        profileImagePath: String? = null,
        biography: String? = null,
        birthDay: String? = null,
        deathDay: String? = null,
        gender: Int? = null,
        knownForDepartment: String? = null,
        placeOfBirth: String? = null
    ) = ActorDto(
        id = id,
        name = name,
        profileImagePath = profileImagePath,
        biography = biography,
        birthDay = birthDay,
        deathDay = deathDay,
        gender = gender,
        knownForDepartment = knownForDepartment,
        placeOfBirth = placeOfBirth
    )

    private fun createMovieCastDto(
        movieId: Int = 1,
        title: String? = null,
        originalTitle: String? = null,
        posterPath: String? = null,
        releaseDate: String? = null,
        voteAverage: Double? = null,
        overview: String? = null
    ) = MovieCastCreditDto(
        movieId = movieId,
        title = title,
        originalTitle = originalTitle,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        overview = overview
    )

    private fun createTvCastDto(
        tvId: Int = 1,
        name: String? = null,
        originalName: String? = null,
        posterPath: String? = null,
        firstAirDate: String? = null,
        voteAverage: Double? = null,
        overview: String? = null
    ) = TvCastCreditDto(
        tvId = tvId,
        name = name,
        originalName = originalName,
        posterPath = posterPath,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        overview = overview
    )
}