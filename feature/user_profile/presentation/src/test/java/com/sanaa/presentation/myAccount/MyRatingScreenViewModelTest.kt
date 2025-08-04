package com.sanaa.presentation.myAccount

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.myRating.MyRatingScreenEffect
import com.sanaa.presentation.screen.myRating.MyRatingScreenViewModel
import com.sanaa.presentation.screen.myRating.MyRatingTab
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class MyRatingScreenViewModelTest {
    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)
    private lateinit var viewModel: MyRatingScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { preferencesManager.accountId } returns flowOf(123L)
        coEvery { preferencesManager.sessionId } returns flowOf("test_session")
    }

    @Test
    fun `loadRatedMedia should sets isNoInternetConnection to true when there is a network error`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies(any(), any()) } throws NoNetworkException()
        coEvery { manageTvSeriesUseCase.getUserRatedTvSeries(any(), any()) } returns listOf(dummyTvSeries)

        viewModel = MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)

        viewModel.state.test {
            val state = awaitItem()
            if (state.isNoInternetConnection) {
                cancelAndIgnoreRemainingEvents()
            } else {
                withTimeout(3000) {
                    awaitItem().also {
                        assertThat(it.isNoInternetConnection).isTrue()
                    }
                }
            }
        }
    }


    @Test
    fun `onBackClick should emit NavigateBack effect when user clicks back button`() = runTest {
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        viewModel.onBackClick()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MyRatingScreenEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTabSelected updates the selectedTab in state`() = runTest {
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        viewModel.onTabSelected(MyRatingTab.TV_SHOWS)
        assertThat(viewModel.state.value.selectedTab).isEqualTo(MyRatingTab.TV_SHOWS)
    }

    @Test
    fun `onDeleteIconClick for movie success removes item from state`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies(any(), any()) } returns listOf(dummyMovie)
        coEvery { manageTvSeriesUseCase.getUserRatedTvSeries(any(), any()) } returns emptyList()
        coEvery { manageMovieUseCase.deleteMovieRate(any()) } returns true
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        advanceUntilIdle()
        viewModel.onDeleteIconClick(dummyMovie.id, MediaTypeUi.MOVIE)
        assertThat(viewModel.state.value.ratedMovies).isEmpty()
    }

    @Test
    fun `onDeleteIconClick for movie failure emits error snackbar`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies(any(), any()) } returns listOf(dummyMovie)
        coEvery { manageTvSeriesUseCase.getUserRatedTvSeries(any(), any()) } returns emptyList()
        coEvery { manageMovieUseCase.deleteMovieRate(any()) } returns false
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        advanceUntilIdle()
        viewModel.onDeleteIconClick(dummyMovie.id, MediaTypeUi.MOVIE)
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MyRatingScreenEffect.ShowErrorSnackBar)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRetryLoadDetails sets isLoading to true`() = runTest {
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        viewModel.updateState { it.copy(isNoInternetConnection = true) }
        viewModel.onRetryLoadDetails()
        assertThat(viewModel.state.value.isLoading).isTrue()
    }

    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        viewModel =
            MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        viewModel.onMediaClick(dummyMovie.id, MediaTypeUi.MOVIE)
        viewModel.effect.test {
            Truth.assertThat(awaitItem()).isEqualTo(
                MyRatingScreenEffect.NavigateToMediaDetails(
                    dummyMovie.id,
                    MediaTypeUi.MOVIE
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onDeleteIconClick for tv show success removes item from state`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies(any(), any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getUserRatedTvSeries(any(), any()) } returns listOf(dummyTvSeries)
        coEvery { manageTvSeriesUseCase.deleteTvSeriesRate(any()) } returns true

        viewModel = MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        advanceUntilIdle()

        viewModel.state.test {
            viewModel.onDeleteIconClick(dummyTvSeries.id, MediaTypeUi.TV_SHOW)

            skipItems(1)

            val updatedState = awaitItem()
            assertThat(updatedState.ratedTvShows).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteIconClick for tv show throws exception emits error snackbar`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies(any(), any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getUserRatedTvSeries(any(), any()) } returns listOf(dummyTvSeries)
        coEvery { manageTvSeriesUseCase.deleteTvSeriesRate(any()) } throws RuntimeException("Delete failed")

        viewModel = MyRatingScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, preferencesManager)
        advanceUntilIdle()

        viewModel.onDeleteIconClick(dummyTvSeries.id, MediaTypeUi.TV_SHOW)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MyRatingScreenEffect.ShowErrorSnackBar)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        val dummyMovie = Movie(
            id = 1,
            posterImageUrl = "/poster.jpg",
            title = "Dummy Movie",
            genres = emptyList(),
            imdbRating = 7.5f,
            duration = null,
            releaseDate = LocalDate.Companion.parse("2023-01-01"),
            rating = 8
        )
        val dummyTvSeries = TvSeries(
            id = 2,
            title = "Dummy TV Show",
            overview = "An overview",
            releaseDate = LocalDate.Companion.parse("2022-05-15"),
            genres = emptyList(),
            imdbRating = 8.2f,
            posterImageUrl = "/tv_poster.jpg",
            seasonsCount = 1,
            rating = 9
        )
    }
}