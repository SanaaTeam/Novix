package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import entity.Genre
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreMapperTest {

    @Test
    fun `test GenreUiState toDomain mapping with valid data`() {
        val uiState = GenreUiState(id = 1, name = "Action")
        val domain = uiState.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Action", domain.name)
    }

    @Test
    fun `test Genre toState mapping with valid data`() {
        val domain = Genre(id = 2, name = "Drama")
        val uiState = domain.toState()

        assertEquals(2, uiState.id)
        assertEquals("Drama", uiState.name)
    }

    @Test
    fun `test GenreUiState toDomain with empty name`() {
        val uiState = GenreUiState(id = 3, name = "")
        val domain = uiState.toDomain()

        assertEquals(3, domain.id)
        assertEquals("", domain.name)
    }

    @Test
    fun `test Genre toState with empty name`() {
        val domain = Genre(id = 4, name = "")
        val uiState = domain.toState()

        assertEquals(4, uiState.id)
        assertEquals("", uiState.name)
    }

}
