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
import usecase.search.MediaFilters

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
    fun `onYearRangeChanged() should change range state when called`() = runTest {
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
    fun `onGenreSelected() should change genre state when called`() = runTest {
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
    fun `onGenreSelected() should remove genre state when called tiwce`() = runTest {
        // Given
        val genre = Genre.ACTION

        // When
        filterViewModel.onGenreSelected(genre)
        filterViewModel.onGenreSelected(genre)

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(
                selectedGenres = emptySet(),
                isDefaultState = false
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onRatingChanged() should change rate state when called`() = runTest {
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
    fun `onClearFilters() should clear filter by set values to default when called`() = runTest {
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
    fun `onApplyClicked() should init media filter when filters are default`() =
        runTest {
            // When
            filterViewModel.onApplyClicked()

            // Then
            filterViewModel.filterResult.test {
                val item = awaitItem()
                Truth.assertThat(item).isEqualTo(null)
            }
        }

    @Test
    fun `onApplyClicked() should change the media filter when filters are default`() =
        runTest {
            // Given
            val rate = 1f

            // When
            filterViewModel.onRatingChanged(rate.toInt())
            filterViewModel.onApplyClicked()

            // Then
            filterViewModel.filterResult.test {
                val item = awaitItem()
                Truth.assertThat(item).isEqualTo(
                    MediaFilters(
                        startYear = 1980,
                        endYear = 2025,
                        imdbRating = rate
                    )
                )
            }
        }

    @Test
    fun `onApplyClicked() should change the media filter without rating when rate sent are 0`() =
        runTest {
            // Given
            val rate = 0f

            // When
            filterViewModel.onRatingChanged(rate.toInt())
            filterViewModel.onApplyClicked()

            // Then
            filterViewModel.filterResult.test {
                val item = awaitItem()
                Truth.assertThat(item).isEqualTo(
                    MediaFilters(
                        startYear = 1980,
                        endYear = 2025,
                        imdbRating = null
                    )
                )
            }
        }

}