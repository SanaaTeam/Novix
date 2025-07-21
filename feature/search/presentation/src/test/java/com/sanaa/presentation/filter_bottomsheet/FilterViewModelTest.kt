package com.sanaa.presentation.filter_bottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.service.GenreLocalizer
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class FilterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val genreLocalizer: GenreLocalizer = mockk(relaxed = true)
    private lateinit var filterViewModel: FilterViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { genreLocalizer.getLocalizedName(any()) } answers { firstArg() }
        filterViewModel = FilterViewModel(testDispatcher, genreLocalizer)
    }

    @AfterEach
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `onYearRangeChanged() should update uiState with the new range`() = runTest {
        // Given
        val newRange = 1995f..2012f

        // When
        filterViewModel.onYearRangeChanged(newRange)

        // Then
        filterViewModel.uiState.test {
            val updatedState = awaitItem()
            assertThat(updatedState.yearRange).isEqualTo(newRange)
            assertThat(updatedState.isDefaultState).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreSelected() should add genre to selectedGenres when it is not already selected`() = runTest {
        // Given
        val genre = Genre.ACTION.name

        // When
        filterViewModel.onGenreSelected(genre)

        // Then
        filterViewModel.uiState.test {
            val updatedState = awaitItem()
            assertThat(updatedState.selectedGenres).contains(genre)
            assertThat(updatedState.isDefaultState).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreSelected() should remove genre from selectedGenres when it is already selected`() = runTest {
        // Given
        val genre = Genre.ACTION.name
        filterViewModel.onGenreSelected(genre)

        // When
        filterViewModel.onGenreSelected(genre)

        // Then
        filterViewModel.uiState.test {
            val updatedState = awaitItem()
            assertThat(updatedState.selectedGenres).doesNotContain(genre)
            assertThat(updatedState.isDefaultState).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRatingChanged() should update uiState with the new rating`() = runTest {
        // Given
        val newRating = 8

        // When the rating is changed
        filterViewModel.onRatingChanged(newRating)

        // Then
        filterViewModel.uiState.test {
            val updatedState = awaitItem()
            assertThat(updatedState.imdbRating).isEqualTo(newRating)
            assertThat(updatedState.isDefaultState).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onClearFilters() should reset uiState to its default values`() = runTest {
        // Given
        filterViewModel.onYearRangeChanged(2000f..2010f)
        filterViewModel.onGenreSelected(Genre.DRAMA.name)
        filterViewModel.onRatingChanged(5)

        // When
        filterViewModel.onClearFilters()

        // Then
        filterViewModel.uiState.test {
            val clearedState = awaitItem()
            val expectedState = FilterUiState(
                allGenres = filterViewModel.uiState.value.allGenres,
                isLoading = false,
                isDefaultState = true,
            )
            assertThat(clearedState).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }
}