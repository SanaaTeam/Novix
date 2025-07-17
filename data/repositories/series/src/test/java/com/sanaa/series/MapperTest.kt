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
import com.sanaa.series.mapper.getProfileImageUrl
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
    fun `TvSeriesDto toEntity maps correctly`() {
        val dto = TvSeriesDto(
            id = 1,
            name = "My Series",
            overview = "Overview text",
            posterPath = "/poster.jpg",
            voteAverage = 8.5f,
            firstAirDate = "2023-06-01",
            genres = listOf(GenreDto(28, "Action"), GenreDto(35, "Comedy")),
            seasonsCount = 3
        )

        val entity = dto.toEntity()

        assertEquals(1, entity.id)
        assertEquals("My Series", entity.title)
        assertEquals("Overview text", entity.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", entity.posterImageUrl)
        assertEquals(8.5f, entity.imdbRating)
        assertEquals(LocalDate.parse("2023-06-01"), entity.releaseDate)
        assertEquals(2, entity.genres.size)
        assertEquals(3, entity.seasonsCount)
        assertTrue(entity.genres.contains(Genre.ACTION))
        assertTrue(entity.genres.contains(Genre.COMEDY))
    }

    @Test
    fun `SeasonDto toEntity maps correctly`() {
        val episodeDto = EpisodeDto(1, "Ep 1", "Desc", 1, 1, 7.0f, "2023-01-01", 60)
        val seasonDto = SeasonDto(10, "Season 1", "Season overview", 1, listOf(episodeDto))

        val season = seasonDto.toEntity()

        assertEquals(10, season.id)
        assertEquals("Season 1", season.title)
        assertEquals("Season overview", season.overview)
        assertEquals(1, season.number)
        assertEquals(1, season.episodes.size)
        assertEquals("Ep 1", season.episodes[0].title)
    }

    @Test
    fun `EpisodeDto toEntity maps correctly`() {
        val dto = EpisodeDto(100, "Episode 1", "Some overview", 2, 3, 8.2f, "2023-04-20", 60)

        val entity = dto.toEntity()

        assertEquals(100, entity.id)
        assertEquals("Episode 1", entity.title)
        assertEquals("Some overview", entity.overview)
        assertEquals(2, entity.seasonNumber)
        assertEquals(3, entity.number)
        assertEquals(8.2f, entity.imdbRating)
        assertEquals(60, entity.durationMinutes)
        assertEquals(LocalDate.parse("2023-04-20"), entity.releaseDate)
    }

    @Test
    fun `GenreDto toEntity returns correct Genre`() {
        assertEquals(Genre.ACTION, GenreDto(28, "Action").toEntity())
        assertEquals(Genre.COMEDY, GenreDto(35, "Comedy").toEntity())
        assertEquals(Genre.DRAMA, GenreDto(18, "Drama").toEntity())
        assertEquals(Genre.DRAMA, GenreDto(999, "Unknown").toEntity())
    }

    @Test
    fun `Genre toDtoId returns correct id`() {
        assertEquals(28, Genre.ACTION.toDtoId())
        assertEquals(35, Genre.COMEDY.toDtoId())
        assertEquals(18, Genre.DRAMA.toDtoId())
        assertEquals(10759, Genre.ACTION_AND_ADVENTURE.toDtoId())
    }

    @Test
    fun `buildPosterUrl returns correct full URL`() {
        val url = buildPosterUrl("/abc.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500/abc.jpg", url)
    }

    @Test
    fun `TvSeriesVideoDto toEntity returns correct YouTube url`() {
        val dtoYoutube = TvSeriesVideoDto("123", "key123", "Trailer", "YouTube", "Trailer")
        val dtoOther = TvSeriesVideoDto("456", "key456", "Trailer2", "Vimeo", "Trailer")

        assertEquals("https://www.youtube.com/watch?v=key123", dtoYoutube.toEntity())
        assertEquals("", dtoOther.toEntity())
    }

    @Test
    fun `TvSeriesImageDto toEntity returns correct image url`() {
        val dto = TvSeriesImageDto("image_path.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500image_path.jpg", dto.toEntity())
    }

    @Test
    fun `ReviewDto toEntity maps fields correctly`() {
        val dto = ReviewDto(
            id = "123",
            content = "Great review!",
            authorDetails = AuthorDetailsDto(
                name = "Haider",
                username = "haider123",
                avatarPath = "/avatar.png",
                rating = 4.5f
            ),
            createdAt = "2024-07-15T12:34:56.000Z"
        )

        val result = dto.toEntity()

        assertEquals(0, result.id)
        assertEquals("Great review!", result.content)
        assertEquals("Haider", result.authorName)
        assertEquals("haider123", result.userHandle)
        assertEquals("/avatar.png", result.avatarUrl)
        assertEquals(4.5f, result.rating)
        assertEquals(LocalDate(2024, 7, 15), result.createdDate)
    }

    @Test
    fun `ReviewDto toEntity handles null avatarPath and rating`() {
        val dto = ReviewDto(
            id = "123",
            content = "Nice!",
            authorDetails = AuthorDetailsDto(
                name = "Sara",
                username = "sara456",
                avatarPath = null,
                rating = null
            ),
            createdAt = "2023-01-05T08:00:00Z"
        )

        val result = dto.toEntity()

        assertEquals("Sara", result.authorName)
        assertEquals("sara456", result.userHandle)
        assertNull(result.avatarUrl)
        assertNull(result.rating)
        assertEquals(LocalDate(2023, 1, 5), result.createdDate)
    }

    @Test
    fun `toEntity maps all fields correctly`() {
        val dto = ActorDto(
            id = 1,
            name = "Bryan Cranston",
            character = "Walter White",
            profilePath = "/bryan.jpg",
            gender = 0
        )

        val result = dto.toEntity()

        assertEquals(1, result.id)
        assertEquals("Bryan Cranston", result.name)
        assertEquals("Walter White", result.character)
        assertEquals("https://image.tmdb.org/t/p/w185/bryan.jpg", result.imageUrl)
        assertEquals(Actor.Gender.MALE, result.gender)
        assertNull(result.region)
        assertNull(result.lastShow)
        assertNull(result.birthDate)
        assertNull(result.deathDate)
        assertNull(result.placeOfBirth)
        assertEquals("", result.biography)
    }

    @Test
    fun `apiGenderMapping maps known and unknown IDs correctly`() {
        assertEquals(Actor.Gender.MALE, apiGenderMapping(0))
        assertEquals(Actor.Gender.FEMALE, apiGenderMapping(1))
        assertEquals(Actor.Gender.MALE, apiGenderMapping(2)) // default fallback
    }

    @Test
    fun `getProfileImageUrl formats URL correctly`() {
        val url = getProfileImageUrl("/path.jpg")
        assertEquals("https://image.tmdb.org/t/p/w185/path.jpg", url)
    }

    @Test
    fun `toEntity maps known ids to genres`() {
        val mappings = mapOf(
            28 to Genre.ACTION,
            12 to Genre.ADVENTURE,
            16 to Genre.ANIMATION,
            35 to Genre.COMEDY,
            80 to Genre.CRIME,
            99 to Genre.DOCUMENTARY,
            18 to Genre.DRAMA,
            10751 to Genre.FAMILY,
            14 to Genre.FANTASY,
            36 to Genre.HISTORY,
            27 to Genre.HORROR,
            10402 to Genre.MUSIC,
            9648 to Genre.MYSTERY,
            10763 to Genre.NEWS,
            10764 to Genre.REALITY,
            10749 to Genre.ROMANCE,
            878 to Genre.SCIENCE_FICTION,
            10765 to Genre.SCI_FI_AND_FANTASY,
            10766 to Genre.SOAP,
            10762 to Genre.KIDS,
            10767 to Genre.TALK,
            53 to Genre.THRILLER,
            10768 to Genre.WAR_AND_POLITICS,
            10752 to Genre.WAR,
            10770 to Genre.TV_MOVIE,
            37 to Genre.WESTERN,
            10759 to Genre.ACTION_AND_ADVENTURE
        )

        mappings.forEach { (id, expectedGenre) ->
            val dto = GenreDto(id)
            assertThat(dto.toEntity()).isEqualTo(expectedGenre)
        }
    }

    @Test
    fun `toEntity maps unknown id to DRAMA`() {
        val dto = GenreDto(99999)
        assertThat(dto.toEntity()).isEqualTo(Genre.DRAMA)
    }

    @Test
    fun `toDtoId maps genres to correct ids`() {
        val reverseMappings = mapOf(
            Genre.ACTION to 28,
            Genre.ADVENTURE to 12,
            Genre.ANIMATION to 16,
            Genre.COMEDY to 35,
            Genre.CRIME to 80,
            Genre.DOCUMENTARY to 99,
            Genre.DRAMA to 18,
            Genre.FAMILY to 10751,
            Genre.FANTASY to 14,
            Genre.HISTORY to 36,
            Genre.HORROR to 27,
            Genre.MUSIC to 10402,
            Genre.MYSTERY to 9648,
            Genre.NEWS to 10763,
            Genre.REALITY to 10764,
            Genre.ROMANCE to 10749,
            Genre.SCIENCE_FICTION to 878,
            Genre.SCI_FI_AND_FANTASY to 10765,
            Genre.SOAP to 10766,
            Genre.KIDS to 10762,
            Genre.TALK to 10767,
            Genre.THRILLER to 53,
            Genre.WAR_AND_POLITICS to 10768,
            Genre.WAR to 10752,
            Genre.TV_MOVIE to 10770,
            Genre.WESTERN to 37,
            Genre.ACTION_AND_ADVENTURE to 10759
        )

        reverseMappings.forEach { (genre, expectedId) ->
            assertThat(genre.toDtoId()).isEqualTo(expectedId)
        }
    }
}
