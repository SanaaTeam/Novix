package com.sanaa.vod.mapper.media


import entity.Genre
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenreMapperTest {


    @Test
    fun `Genre ACTION maps to 28`() = assertEquals(28, Genre.ACTION.toDtoId())

    @Test
    fun `Genre ADVENTURE maps to 12`() = assertEquals(12, Genre.ADVENTURE.toDtoId())

    @Test
    fun `Genre COMEDY maps to 35`() = assertEquals(35, Genre.COMEDY.toDtoId())

    @Test
    fun `Genre DRAMA maps to 18`() = assertEquals(18, Genre.DRAMA.toDtoId())

    @Test
    fun `Genre HORROR maps to 27`() = assertEquals(27, Genre.HORROR.toDtoId())

    @Test
    fun `Genre SCIENCE_FICTION maps to 878`() = assertEquals(878, Genre.SCIENCE_FICTION.toDtoId())

    @Test
    fun `Genre FANTASY maps to 14`() = assertEquals(14, Genre.FANTASY.toDtoId())

    @Test
    fun `Genre ROMANCE maps to 10749`() = assertEquals(10749, Genre.ROMANCE.toDtoId())

    @Test
    fun `Genre THRILLER maps to 53`() = assertEquals(53, Genre.THRILLER.toDtoId())

    @Test
    fun `Genre DOCUMENTARY maps to 99`() = assertEquals(99, Genre.DOCUMENTARY.toDtoId())

    @Test
    fun `Genre ANIMATION maps to 16`() = assertEquals(16, Genre.ANIMATION.toDtoId())

    @Test
    fun `Genre CRIME maps to 80`() = assertEquals(80, Genre.CRIME.toDtoId())

    @Test
    fun `Genre FAMILY maps to 10751`() = assertEquals(10751, Genre.FAMILY.toDtoId())

    @Test
    fun `Genre HISTORY maps to 36`() = assertEquals(36, Genre.HISTORY.toDtoId())

    @Test
    fun `Genre KIDS maps to 10762`() = assertEquals(10762, Genre.KIDS.toDtoId())

    @Test
    fun `Genre MYSTERY maps to 9648`() = assertEquals(9648, Genre.MYSTERY.toDtoId())

    @Test
    fun `Genre MUSIC maps to 10402`() = assertEquals(10402, Genre.MUSIC.toDtoId())

    @Test
    fun `Genre NEWS maps to 10763`() = assertEquals(10763, Genre.NEWS.toDtoId())

    @Test
    fun `Genre REALITY maps to 10764`() = assertEquals(10764, Genre.REALITY.toDtoId())

    @Test
    fun `Genre SOAP maps to 10766`() = assertEquals(10766, Genre.SOAP.toDtoId())

    @Test
    fun `Genre TALK maps to 10767`() = assertEquals(10767, Genre.TALK.toDtoId())

    @Test
    fun `Genre WAR maps to 10752`() = assertEquals(10752, Genre.WAR.toDtoId())

    @Test
    fun `Genre WAR_AND_POLITICS maps to 10768`() =
        assertEquals(10768, Genre.WAR_AND_POLITICS.toDtoId())

    @Test
    fun `Genre WESTERN maps to 37`() = assertEquals(37, Genre.WESTERN.toDtoId())

    @Test
    fun `Genre TV_MOVIE maps to 10770`() = assertEquals(10770, Genre.TV_MOVIE.toDtoId())

    @Test
    fun `Genre ACTION_AND_ADVENTURE maps to 10759`() =
        assertEquals(10759, Genre.ACTION_AND_ADVENTURE.toDtoId())

    @Test
    fun `Genre SCI_FI_AND_FANTASY maps to 10765`() =
        assertEquals(10765, Genre.SCI_FI_AND_FANTASY.toDtoId())

    // === toGenre() Tests ===

    @Test
    fun `ID 28 maps to Genre ACTION`() = assertEquals(Genre.ACTION, 28.toGenre())

    @Test
    fun `ID 12 maps to Genre ADVENTURE`() = assertEquals(Genre.ADVENTURE, 12.toGenre())

    @Test
    fun `ID 35 maps to Genre COMEDY`() = assertEquals(Genre.COMEDY, 35.toGenre())

    @Test
    fun `ID 18 maps to Genre DRAMA`() = assertEquals(Genre.DRAMA, 18.toGenre())

    @Test
    fun `ID 27 maps to Genre HORROR`() = assertEquals(Genre.HORROR, 27.toGenre())

    @Test
    fun `ID 878 maps to Genre SCIENCE_FICTION`() =
        assertEquals(Genre.SCIENCE_FICTION, 878.toGenre())

    @Test
    fun `ID 14 maps to Genre FANTASY`() = assertEquals(Genre.FANTASY, 14.toGenre())

    @Test
    fun `ID 10749 maps to Genre ROMANCE`() = assertEquals(Genre.ROMANCE, 10749.toGenre())

    @Test
    fun `ID 53 maps to Genre THRILLER`() = assertEquals(Genre.THRILLER, 53.toGenre())

    @Test
    fun `ID 99 maps to Genre DOCUMENTARY`() = assertEquals(Genre.DOCUMENTARY, 99.toGenre())

    @Test
    fun `ID 16 maps to Genre ANIMATION`() = assertEquals(Genre.ANIMATION, 16.toGenre())

    @Test
    fun `ID 80 maps to Genre CRIME`() = assertEquals(Genre.CRIME, 80.toGenre())

    @Test
    fun `ID 10751 maps to Genre FAMILY`() = assertEquals(Genre.FAMILY, 10751.toGenre())

    @Test
    fun `ID 36 maps to Genre HISTORY`() = assertEquals(Genre.HISTORY, 36.toGenre())

    @Test
    fun `ID 10762 maps to Genre KIDS`() = assertEquals(Genre.KIDS, 10762.toGenre())

    @Test
    fun `ID 9648 maps to Genre MYSTERY`() = assertEquals(Genre.MYSTERY, 9648.toGenre())

    @Test
    fun `ID 10402 maps to Genre MUSIC`() = assertEquals(Genre.MUSIC, 10402.toGenre())

    @Test
    fun `ID 10763 maps to Genre NEWS`() = assertEquals(Genre.NEWS, 10763.toGenre())

    @Test
    fun `ID 10764 maps to Genre REALITY`() = assertEquals(Genre.REALITY, 10764.toGenre())

    @Test
    fun `ID 10766 maps to Genre SOAP`() = assertEquals(Genre.SOAP, 10766.toGenre())

    @Test
    fun `ID 10767 maps to Genre TALK`() = assertEquals(Genre.TALK, 10767.toGenre())

    @Test
    fun `ID 10752 maps to Genre WAR`() = assertEquals(Genre.WAR, 10752.toGenre())

    @Test
    fun `ID 10768 maps to Genre WAR_AND_POLITICS`() =
        assertEquals(Genre.WAR_AND_POLITICS, 10768.toGenre())

    @Test
    fun `ID 37 maps to Genre WESTERN`() = assertEquals(Genre.WESTERN, 37.toGenre())

    @Test
    fun `ID 10770 maps to Genre TV_MOVIE`() = assertEquals(Genre.TV_MOVIE, 10770.toGenre())

    @Test
    fun `ID 10759 maps to Genre ACTION_AND_ADVENTURE`() =
        assertEquals(Genre.ACTION_AND_ADVENTURE, 10759.toGenre())

    @Test
    fun `ID 10765 maps to Genre SCI_FI_AND_FANTASY`() =
        assertEquals(Genre.SCI_FI_AND_FANTASY, 10765.toGenre())


    @Test
    fun `ID 999 returns null`() = assertNull(999.toGenre())
}