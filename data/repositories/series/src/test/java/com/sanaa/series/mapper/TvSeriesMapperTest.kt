package com.sanaa.series.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.TvSeriesImageDto
import com.sanaa.series.dto.TvSeriesVideoDto
import entity.Actor
import entity.Genre
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TvSeriesMapperTest {

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

    @Test
    fun `should return youtube url when site is YouTube`() {
        val result = createVideoDto(site = "YouTube").toEntity()
        assertThat(result).isEqualTo("https://www.youtube.com/watch?v=key123")
    }

    @Test
    fun `should return empty url when site is not YouTube`() {
        val result = createVideoDto(site = "Vimeo").toEntity()
        assertThat(result).isEqualTo("")
    }

    //SeasonDto
    @Test
    fun `SeasonDto toEntity maps id correctly`() {
        val dto = createSeasonDto(id = 10)
        val entity = dto.toEntity()
        assertEquals(10, entity.id)
    }

    @Test
    fun `SeasonDto toEntity maps title correctly`() {
        val dto = createSeasonDto(name = "Season 1")
        val entity = dto.toEntity()
        assertEquals("Season 1", entity.title)
    }

    @Test
    fun `SeasonDto toEntity maps overview correctly`() {
        val dto = createSeasonDto(overview = "Season overview")
        val entity = dto.toEntity()
        assertEquals("Season overview", entity.overview)
    }

    @Test
    fun `SeasonDto toEntity maps season number correctly`() {
        val dto = createSeasonDto(seasonNumber = 1)
        val entity = dto.toEntity()
        assertEquals(1, entity.number)
    }

    @Test
    fun `SeasonDto toEntity maps episodes size correctly`() {
        val dto = createSeasonDto(episodes = listOf(createEpisodeDto()))
        val entity = dto.toEntity()
        assertEquals(1, entity.episodes.size)
    }

    @Test
    fun `SeasonDto toEntity maps episode title correctly`() {
        val dto = createSeasonDto(episodes = listOf(createEpisodeDto(name = "Ep 1")))
        val entity = dto.toEntity()
        assertEquals("Ep 1", entity.episodes[0].title)
    }

    //EpisodeDto
    @Test
    fun `EpisodeDto toEntity maps id correctly`() {
        val dto = createEpisodeDto(id = 100)
        val entity = dto.toEntity()
        assertEquals(100, entity.id)
    }

    @Test
    fun `EpisodeDto toEntity maps title correctly`() {
        val dto = createEpisodeDto(name = "Episode 1")
        val entity = dto.toEntity()
        assertEquals("Episode 1", entity.title)
    }

    @Test
    fun `EpisodeDto toEntity maps overview correctly`() {
        val dto = createEpisodeDto(overview = "Some overview")
        val entity = dto.toEntity()
        assertEquals("Some overview", entity.overview)
    }

    @Test
    fun `EpisodeDto toEntity maps seasonNumber correctly`() {
        val dto = createEpisodeDto(seasonNumber = 2)
        val entity = dto.toEntity()
        assertEquals(2, entity.seasonNumber)
    }

    @Test
    fun `EpisodeDto toEntity maps episode number correctly`() {
        val dto = createEpisodeDto(episodeNumber = 3)
        val entity = dto.toEntity()
        assertEquals(3, entity.number)
    }

    @Test
    fun `EpisodeDto toEntity maps imdbRating correctly`() {
        val dto = createEpisodeDto(voteAverage = 8.2f)
        val entity = dto.toEntity()
        assertEquals(8.2f, entity.imdbRating)
    }

    @Test
    fun `EpisodeDto toEntity maps duration correctly`() {
        val dto = createEpisodeDto(runtime = 60)
        val entity = dto.toEntity()
        assertEquals(60, entity.durationMinutes)
    }

    @Test
    fun `EpisodeDto toEntity maps releaseDate correctly`() {
        val dto = createEpisodeDto(airDate = "2023-04-20")
        val entity = dto.toEntity()
        assertEquals(LocalDate.parse("2023-04-20"), entity.releaseDate)
    }

    @Test
    fun `buildPosterUrl returns correct full URL`() {
        val url = buildPosterUrl("/abc.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500/abc.jpg", url)
    }




    @Test
    fun `TvSeriesImageDto toEntity returns correct image url`() {
        val dto = TvSeriesImageDto("image_path.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500image_path.jpg", dto.toEntity())
    }

//apiGenderMapping

    @Test
    fun `should return MALE when gender id is 0`() {
        val result = apiGenderMapping(0)
        assertThat(result).isEqualTo(Actor.Gender.MALE)
    }

    @Test
    fun `should return FEMALE when gender id is 1`() {
        val result = apiGenderMapping(1)
        assertThat(result).isEqualTo(Actor.Gender.FEMALE)
    }

    @Test
    fun `should default to MALE when gender id is unknown`() {
        val result = apiGenderMapping(999)
        assertThat(result).isEqualTo(Actor.Gender.MALE)
    }


    @Test
    fun `getFullImageUrl formats URL correctly`() {
        val url = getFullImageUrl("/path.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500/path.jpg", url)
    }

    @Test
    fun `getFullImageUrl returns empty string when path is empty`() {
        val url = getFullImageUrl("")
        assertEquals("", url)
    }

    //Genre

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

    @Test
    fun `toEntity maps unknown id to DRAMA`() {
        val dto = GenreDto(99999)
        assertThat(dto.toEntity()).isEqualTo(Genre.DRAMA)
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

    private fun createVideoDto(
        id: String = "123",
        key: String = "key123",
        name: String = "Trailer",
        site: String = "YouTube",
        type: String = "Trailer"
    ): TvSeriesVideoDto {
        return TvSeriesVideoDto(
            id = id,
            key = key,
            name = name,
            site = site,
            type = type
        )
    }

    private fun createSeasonDto(
        id: Int = 10,
        name: String = "Season 1",
        overview: String = "Season overview",
        seasonNumber: Int = 1,
        episodes: List<EpisodeDto> = listOf(createEpisodeDto())
    ) = SeasonDto(
        id = id,
        name = name,
        overview = overview,
        seasonNumber = seasonNumber,
        episodes = episodes
    )

    private fun createEpisodeDto(
        id: Int = 1,
        name: String = "Ep 1",
        overview: String = "Desc",
        seasonNumber: Int = 1,
        episodeNumber: Int = 1,
        voteAverage: Float = 7.0f,
        airDate: String = "2023-01-01",
        runtime: Int = 60
    ) = EpisodeDto(
        id = id,
        name = name,
        overview = overview,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        voteAverage = voteAverage,
        airDate = airDate,
        runtime = runtime
    )
}