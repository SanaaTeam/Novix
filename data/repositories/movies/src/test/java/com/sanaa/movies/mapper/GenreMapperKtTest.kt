package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import entity.Genre
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GenreMapperKtTest{
    @Test
    fun `Genre maps to correct ID and back`() {
        val genre = Genre.ACTION
        val id = genre.toDtoId()
        val result = id.toGenre()

        assertThat(id).isEqualTo(28)
        assertThat(result).isEqualTo(Genre.ACTION)
    }

}