package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import entity.Genre
import org.junit.jupiter.api.Test

class GenreMapperKtTest{
    @Test
    fun `should map all genres to correct ID and back`() {
        val genreToIdMap = mapOf(
            Genre.ACTION to 28,
            Genre.ADVENTURE to 12,
            Genre.COMEDY to 35,
            Genre.DRAMA to 18,
            Genre.HORROR to 27,
            Genre.SCIENCE_FICTION to 878,
            Genre.FANTASY to 14,
            Genre.ROMANCE to 10749,
            Genre.THRILLER to 53,
            Genre.DOCUMENTARY to 99,
            Genre.ANIMATION to 16,
            Genre.CRIME to 80,
            Genre.FAMILY to 10751,
            Genre.HISTORY to 36,
            Genre.KIDS to 10762,
            Genre.MYSTERY to 9648,
            Genre.MUSIC to 10402,
            Genre.NEWS to 10763,
            Genre.REALITY to 10764,
            Genre.SOAP to 10766,
            Genre.TALK to 10767,
            Genre.WAR to 10752,
            Genre.WAR_AND_POLITICS to 10768,
            Genre.WESTERN to 37,
            Genre.TV_MOVIE to 10770,
            Genre.ACTION_AND_ADVENTURE to 10759,
            Genre.SCI_FI_AND_FANTASY to 10765
        )

        for ((genre, expectedId) in genreToIdMap) {
            val actualId = genre.toDtoId()
            val reversedGenre = actualId.toGenre()

            assertThat(actualId).isEqualTo(expectedId)
            assertThat(reversedGenre).isEqualTo(genre)
        }
    }
    @Test
    fun `should return null when genre ID is invalid`() {
        val invalidIds = listOf(-1, 0, 999, 1234)
        for (id in invalidIds) {
            val genre = id.toGenre()
            assertThat(genre).isNull()
        }
    }

}