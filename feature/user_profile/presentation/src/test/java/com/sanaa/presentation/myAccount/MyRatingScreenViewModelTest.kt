package com.sanaa.presentation.myAccount

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.myRating.MyRatingScreenEffect
import com.sanaa.presentation.screen.myRating.MyRatingScreenViewModel
import com.sanaa.presentation.screen.myRating.MyRatingTab
import entity.Movie
import entity.TvShow
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
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.VodStringProvider
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class MyRatingScreenViewModelTest {
    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvSeriesUseCase: ManageTvShowUseCase = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk(relaxed = true)
    private lateinit var viewModel: MyRatingScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { preferencesManager.accountId } returns flowOf(123L)
        coEvery { preferencesManager.sessionId } returns flowOf("test_session")

        coEvery { stringProvider.deleteRatingFailed } returns "Failed to delete rating."
        coEvery { stringProvider.deleteRatingSuccess } returns "Rating deleted successfully."

        coEvery { manageMovieUseCase.getUserRatedMovies() } returns listOf(dummyMovie)
        coEvery { manageTvSeriesUseCase.getRatedTvShows(any(), any()) } returns listOf(dummyTvSeries)
    }

    @Test
    fun `loadRatedMedia should set isNoInternetConnection to true when a network error occurs`() = runTest {
        coEvery { manageMovieUseCase.getUserRatedMovies() } throws NoNetworkException()
        coEvery { manageTvSeriesUseCase.getRatedTvShows(any(), any()) } returns emptyList()

        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isNoInternetConnection).isTrue()
            assertThat(state.isLoading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MyRatingScreenEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTabSelected updates the selectedTab in state`() = runTest {
        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )
        viewModel.onTabSelected(MyRatingTab.TV_SHOWS)
        assertThat(viewModel.state.value.selectedTab).isEqualTo(MyRatingTab.TV_SHOWS)
    }

    @Test
    fun `onDeleteIconClick for movie failure emits error snackbar`() = runTest {
        coEvery { manageMovieUseCase.deleteMovieRate(any()) } throws RuntimeException("Deletion failed")
        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onDeleteIconClick(dummyMovie.id, MediaTypeUi.MOVIE)
            advanceUntilIdle()

            assertThat(viewModel.state.value.snackBarData?.message).isEqualTo(stringProvider.deleteRatingFailed)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )

        viewModel.effect.test {
            viewModel.onMediaClick(dummyMovie.id, MediaTypeUi.MOVIE)
            assertThat(awaitItem()).isEqualTo(
                MyRatingScreenEffect.NavigateToMediaDetails(
                    dummyMovie.id,
                    MediaTypeUi.MOVIE
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteIconClick for tv show failure emits error snackbar`() = runTest {
        coEvery { manageTvSeriesUseCase.deleteTvShowRate(any()) } throws RuntimeException("Delete failed")
        viewModel = MyRatingScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            preferencesManager,
            stringProvider,
            testDispatcher
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onDeleteIconClick(dummyTvSeries.id, MediaTypeUi.TV_SHOW)
            advanceUntilIdle()

            assertThat(viewModel.state.value.snackBarData?.message).isEqualTo(stringProvider.deleteRatingFailed)
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
            releaseDate = LocalDate.parse("2023-01-01"),
            rating = 0,
            duration = 120.minutes,
            overview = "This is a placeholder description for the dummy movie.",
            trailerUrl = "https://example.com/trailer.mp4"
        )

        val dummyTvSeries = TvShow(
            id = 2,
            title = "Dummy TV Show",
            overview = "An overview",
            releaseDate = LocalDate.parse("2022-05-15"),
            genres = emptyList(),
            imdbRating = 8.2f,
            posterImageUrl = "/tv_poster.jpg",
            seasonsCount = 1,
            rating = 9
        )
    }
}