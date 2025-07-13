package com.sanaa.presentation.filter_bottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FilterViewModelTest {
    private lateinit var filterViewModel: FilterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        filterViewModel = FilterViewModel(testDispatcher)
    }

    @Test
    fun `onYearRangeChanged()`() = runTest {
        // Given
        val range = 1995f..2012f

        // When
        filterViewModel.onYearRangeChanged(range)

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(yearRange = range, isDefaultState = false)
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onGenreSelected()`() = runTest {
        // Given
        val genre = Genre.ACTION

        // When
        filterViewModel.onGenreSelected(genre)

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(
                selectedGenres = setOf(genre).toMutableSet(),
                isDefaultState = false
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onRatingChanged()`() = runTest {
        // Given
        val rate = 10

        // When
        filterViewModel.onRatingChanged(rate)

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(
                imdbRating = rate,
                isDefaultState = false
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onClearFilters()`() = runTest {
        // When
        filterViewModel.onClearFilters()

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(
                allGenres = filterViewModel.uiState.value.allGenres,
                isLoading = false,
                isDefaultState = true,
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onApplyClicked()`() = runTest {
        // When
        filterViewModel.onApplyClicked()

        // Then
        filterViewModel.filterResult.test {
            val item = awaitItem()
            Truth.assertThat(item).isEqualTo(null)
        }
    }

}