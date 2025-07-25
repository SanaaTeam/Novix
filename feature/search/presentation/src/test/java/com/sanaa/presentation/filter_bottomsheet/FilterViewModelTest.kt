package com.sanaa.presentation.filter_bottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import com.sanaa.presentation.screen.state.mapper.toDomain
import com.sanaa.presentation.screen.state.mapper.toState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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

        filterViewModel.onYearRangeChanged(range)

        filterViewModel.state.test {
            val item = awaitItem()
            val expected = FilterUiState(yearRange = range)
            Truth.assertThat(item.yearRange).isEqualTo(expected.yearRange)
        }
    }

    @Test
    fun `onGenreSelected() should change genre state when called`() = runTest {
        val genre = genres[0]

        filterViewModel.onGenreSelected(genre)

        filterViewModel.state.test {
            val item = awaitItem()
            val expected = FilterUiState(selectedGenres = setOf(genre).toMutableSet())
            Truth.assertThat(item.selectedGenres).isEqualTo(expected.selectedGenres)
        }
    }

    @Test
    fun `onGenreSelected() should remove genre state when called twice`() = runTest {
        val genre = genres[0]

        filterViewModel.onGenreSelected(genre)
        filterViewModel.onGenreSelected(genre)

        filterViewModel.state.test {
            awaitItem()
            val expected = FilterUiState(selectedGenres = emptySet())
            Truth.assertThat(expected.selectedGenres).isEqualTo(emptySet<String>())
        }
    }

    @Test
    fun `onRatingChanged() should change rate state when called`() = runTest {
        val rate = 10

        filterViewModel.onRatingChanged(rate)

        filterViewModel.state.test {
            val item = awaitItem()
            val expected = FilterUiState(imdbRating = rate)
            Truth.assertThat(expected.imdbRating).isEqualTo(item.imdbRating)
        }
    }

    @Test
    fun `onClearFilters() should clear filter by set values to default when called`() = runTest {
        filterViewModel.onClearFilters()

        filterViewModel.state.test {
            val item = awaitItem()
            val expected = FilterUiState(
                allGenres = filterViewModel.state.value.allGenres,
                isLoading = false,
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onApplyClicked() should init media filter when filters are default`() = runTest {
        filterViewModel.onApplyClicked()

        filterViewModel.filterResult.test {
            val item = awaitItem()
            Truth.assertThat(item).isEqualTo(
                MediaFilters(
                    startYear = 1980,
                    endYear = 2025,
                    genres = emptyList(),
                    imdbRating = 0.0f
                )
            )
        }
    }

    @Test
    fun `onApplyClicked() should return media filters with selected genres`() = runTest {
        val genre = genres[0]
        val rate = 1f

        filterViewModel.onRatingChanged(rate.toInt())
        filterViewModel.onGenreSelected(genre)
        filterViewModel.onApplyClicked()

        filterViewModel.filterResult.test {
            val item = awaitItem()
            Truth.assertThat(item).isEqualTo(
                MediaFilters(
                    startYear = 1980,
                    endYear = 2025,
                    genres = listOf(genre.toDomain()),
                    imdbRating = rate
                )
            )
        }
    }

    @Test
    fun `onApplyClicked should emit media filters with correctly mapped genres`() = runTest {
        val genre = genres[0]
        filterViewModel.onGenreSelected(genre)

        // When
        filterViewModel.onApplyClicked()

        // Then
        filterViewModel.filterResult.test {
            val item = awaitItem()

            Truth.assertThat(item?.genres).containsExactly(genre.toDomain())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onApplyClicked should emit media filters with correct genres after selecting genre`() =
        runTest {
            val genre = genres[0]
            filterViewModel.onGenreSelected(genre)

            filterViewModel.onApplyClicked()

            filterViewModel.filterResult.test {
                val result = awaitItem()
                Truth.assertThat(result?.genres).containsExactly(genre.toDomain())
                cancelAndIgnoreRemainingEvents()
            }
        }
    @Test
    fun `fetchGenres() should update UI state with mapped genres`() = runTest {
        val domainGenres = listOf(
            entity.Genre(id = 1, name = "Action"),
            entity.Genre(id = 2, name = "Drama")
        )

        coEvery { manageMovieUseCase.getMovieGenres() } returns domainGenres
        coEvery { manageTvSeriesUseCase.getSeriesGenres() } returns emptyList()

        filterViewModel = FilterViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        filterViewModel.state.test {
            val state = awaitItem()
            val expected = domainGenres.map { it.toState() }

            Truth.assertThat(state.allGenres).containsExactlyElementsIn(expected)
            cancelAndIgnoreRemainingEvents()
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