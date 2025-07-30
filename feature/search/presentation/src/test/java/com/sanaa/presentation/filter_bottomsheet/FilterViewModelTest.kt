package com.sanaa.presentation.filter_bottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import com.sanaa.presentation.filter_bottomsheet.state.MediaTabFilters
import com.sanaa.presentation.screen.state.mapper.toDomain
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.search.search_param.MediaFilters

class FilterViewModelTest {
    private lateinit var filterViewModel: FilterViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val manageMovieUseCase = mockk<ManageMovieUseCase>(relaxed = true)
    private val manageTvSeriesUseCase = mockk<ManageTvSeriesUseCase>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        filterViewModel = FilterViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
    }

    @Test
    fun `onYearRangeChanged() should change range state when called`() = runTest {
        val range = 1995f..2012f

        filterViewModel.onYearRangeChanged(FilterViewModel.MOVIE_INDEX, range)

        filterViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.movieFilters.yearRange).isEqualTo(range)
        }
    }


    @Test
    fun `onGenreSelected() should add genre correctly`() = runTest {
        val genre = genres[0]

        filterViewModel.onGenreSelected(FilterViewModel.MOVIE_INDEX, genre)
        filterViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.movieFilters.selectedGenres).contains(genre)
        }


    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onGenreSelected() should remove genre correctly`() = runTest {
        val genre = genres[0]

        filterViewModel.onGenreSelected(FilterViewModel.MOVIE_INDEX, genre)
        advanceUntilIdle()
        filterViewModel.onGenreSelected(FilterViewModel.MOVIE_INDEX, genre)
        filterViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.movieFilters.selectedGenres).doesNotContain(genre)
        }
    }

    @Test
    fun `onRatingChanged() should update rating correctly`() = runTest {
        val rating = 7

        filterViewModel.onRatingChanged(FilterViewModel.MOVIE_INDEX, rating)

        filterViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.movieFilters.imdbRating).isEqualTo(rating)
        }
    }


    @Test
    fun `onClearFilters() should reset filters to default`() = runTest {
        filterViewModel.onClearFilters(FilterViewModel.MOVIE_INDEX)

        filterViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.movieFilters).isEqualTo(MediaTabFilters())
        }

        filterViewModel.filterResult.test {
            val emitted = awaitItem()
            Truth.assertThat(emitted.second).isEqualTo(
                MediaFilters(
                    startYear = 1850,
                    endYear = 2025,
                    genres = emptyList(),
                    imdbRating = 0f
                )
            )
        }
    }
    @Test
    fun `onApplyClicked should emit correct filters`() = runTest {
        val genre = genres[0]
        filterViewModel.onGenreSelected(FilterViewModel.MOVIE_INDEX, genre)
        filterViewModel.onRatingChanged(FilterViewModel.MOVIE_INDEX, 8)
        filterViewModel.onYearRangeChanged(FilterViewModel.MOVIE_INDEX, 2000f..2020f)

        filterViewModel.onApplyClicked(FilterViewModel.MOVIE_INDEX)

        filterViewModel.filterResult.test {
            val result = awaitItem()
            Truth.assertThat(result.second).isEqualTo(
                MediaFilters(
                    startYear = 2000,
                    endYear = 2020,
                    genres = listOf(genre.toDomain()),
                    imdbRating = 8f
                )
            )
        }
    }


    private companion object {
        val genres = listOf(
            GenreUiState(
                id = 1,
                name = "Action"
            ),
            GenreUiState(
                id = 2,
                name = "Drama"
            ),
            GenreUiState(
                id = 3,
                name = "Crime"
            ),
        )
    }

}