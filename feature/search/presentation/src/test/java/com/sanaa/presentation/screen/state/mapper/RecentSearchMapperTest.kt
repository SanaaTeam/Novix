package com.sanaa.presentation.screen.state.mapper

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import extensions.now
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Test
import search.usecase.search_param.SearchHistory

class RecentSearchMapperTest {
    @Test
    fun `toUiState should map SearchHistory to RecentSearchUiModel correctly`() {
        // Arrange
        val searchHistory = createSearchHistory(1, "Matrix")

        // Act
        val result = searchHistory.toUiState()

        // Assert
        val expected = createExpectedRecentSearchUiModel(1, "Matrix")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `List toUiState should map list of SearchHistory to list of RecentSearchUiModel`() {
        // Arrange
        val list = listOf(
            createSearchHistory(1, "Matrix"),
            createSearchHistory(2, "Inception")
        )

        // Act
        val result = list.toUiState()

        // Assert
        val expected = listOf(
            createExpectedRecentSearchUiModel(1, "Matrix"),
            createExpectedRecentSearchUiModel(2, "Inception")
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Flow toUiState should map flow of SearchHistory to RecentSearchUiModel`() = runTest {
        // Arrange
        val flow = flowOf(
            listOf(
                createSearchHistory(10, "Oppenheimer"),
                createSearchHistory(20, "Dune")
            )
        )

        val expected = listOf(
            createExpectedRecentSearchUiModel(10, "Oppenheimer"),
            createExpectedRecentSearchUiModel(20, "Dune")
        )

        // Act & Assert
        flow.toUiState().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(expected)
            awaitComplete()
        }
    }

    companion object {
        private fun createSearchHistory(id: Int, query: String): SearchHistory =
            SearchHistory(
                id = id,
                query = query,
                timestamp = LocalDateTime.now()
            )

        private fun createExpectedRecentSearchUiModel(id: Int, title: String): RecentSearchUiModel =
            RecentSearchUiModel(
                id = id,
                title = title
            )
    }
}