package com.sanaa.presentation.filter_bottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.preferences.service.GenreLocalizer
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import search.usecase.search_param.MediaFilters

class FilterViewModelTest {
    private lateinit var filterViewModel: FilterViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val genreLocalizer = mockk<GenreLocalizer>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        filterViewModel = FilterViewModel(testDispatcher, genreLocalizer)
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
            Truth.assertThat(item.yearRange).isEqualTo(expected.yearRange)
        }
    }

    @Test
    fun `onGenreSelected() should change genre state when called`() = runTest {
        // Given
        val genre = Genre.ACTION.name

        // When
        filterViewModel.onGenreSelected(genre)

        // Then
        filterViewModel.uiState.test {
            val item = awaitItem()
            val expected = FilterUiState(
                selectedGenres = setOf(genre).toMutableSet(),
                isDefaultState = false
            )
            Truth.assertThat(item.selectedGenres).isEqualTo(expected.selectedGenres)
        }
    }

    @Test
    fun `onGenreSelected() should remove genre state when called tiwce`() = runTest {
        // Given
        val genre = Genre.ACTION.name

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
            Truth.assertThat(expected.selectedGenres).isEqualTo(emptySet<String>())
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
            Truth.assertThat(expected.imdbRating).isEqualTo(item.imdbRating)
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
    fun `onApplyClicked() should return media filters with selected genres`() =
        runTest {
            // Given
            val rate = 1f
            // When
            filterViewModel.onRatingChanged(rate.toInt())
            filterViewModel.onGenreSelected("Kids")
            filterViewModel.onApplyClicked()

            // Then
            filterViewModel.filterResult.test {
                val item = awaitItem()
                Truth.assertThat(item).isEqualTo(
                    MediaFilters(
                        startYear = 1980,
                        endYear = 2025,
                        genres = listOf(Genre.KIDS),
                        imdbRating = rate
                    )
                )
            }
        }

    @Test
    fun `onApplyClicked should emit media filters with correctly mapped genres`() = runTest {
        // Given
        filterViewModel.onGenreSelected("KIDS")

        // When
        filterViewModel.onApplyClicked()

        // Then
        filterViewModel.filterResult.test {
            val item = awaitItem()

            Truth.assertThat(item?.genres).containsExactly(Genre.KIDS)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onApplyClicked should emit media filters with correct genres after selecting genre`() =
        runTest {
            // Given
            filterViewModel.onGenreSelected("Kids")

            // When
            filterViewModel.onApplyClicked()

            // Then
            filterViewModel.filterResult.test {
                val result = awaitItem()
                Truth.assertThat(result?.genres).containsExactly(Genre.KIDS)
                cancelAndIgnoreRemainingEvents()
            }
        }


}