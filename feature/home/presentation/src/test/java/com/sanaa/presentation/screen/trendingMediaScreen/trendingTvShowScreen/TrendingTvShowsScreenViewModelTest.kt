package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import entity.Genre
import entity.TvSeries
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvSeriesUseCase

class TrendingTvShowsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manageTvSeriesUseCase: ManageTvSeriesUseCase
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)

    private lateinit var viewModel: TrendingTvShowsScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageTvSeriesUseCase = mockk(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch genres and update state on creation`() = runTest {
        coEvery { manageTvSeriesUseCase.getSeriesGenres() } returns genres

        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.genreList).isEqualTo(genres.map { it.toState() })
    }

    @Test
    fun `init should fetch tv show and update state on creation`() = runTest {
        coEvery { manageTvSeriesUseCase.getTrendingTvSeries(any(), any()) } returns tvShows

        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val pagingData = viewModel.state.value.mediaList
        val items = pagingData.asSnapshot()

        assertThat(items.take(tvShows.size)).isEqualTo(tvShows.map { it.toState() })
    }

    @Test
    fun `onGenreClick should fetch media and update state with media list for the passed genre when available`() =
        runTest {
            val genreId = 2
            coEvery {
                manageTvSeriesUseCase.getTrendingTvSeries(any(), genreId)
            } returns tvShows
            viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onGenreClick(genreId)
            testDispatcher.scheduler.advanceUntilIdle()
            val pagingData = viewModel.state.value.mediaList
            val items = pagingData.asSnapshot()

            assertThat(items.take(tvShows.size)).isEqualTo(tvShows.map { it.toState() })
        }

    @Test
    fun `fetchGenres should handles error and updates state when throw exception`() = runTest {
        val exceptionMessage = "error"
        coEvery { manageTvSeriesUseCase.getSeriesGenres() } throws RuntimeException(exceptionMessage)

        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo(exceptionMessage)
    }

    @Test
    fun `fetchGenres should update isNoInternetConnection state to true when throw NoNetworkException`() =
        runTest {
            coEvery { manageTvSeriesUseCase.getSeriesGenres() } throws NoNetworkException()

            viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.state.value.isNoInternetConnection).isTrue()
        }

    @Test
    fun `onSaveIconClick should update state to show bottom sheet when called`() = runTest {
        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSaveIconClick(media)

        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onGenreClick should not fetchMedia when click on same genre`() = runTest {
        coEvery { manageTvSeriesUseCase.getTrendingTvSeries(any(), any()) } returns listOf(mockk())
        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val selectedGenre = viewModel.state.value.selectedGenreId

        viewModel.onGenreClick(selectedGenre)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { manageTvSeriesUseCase.getTrendingTvSeries(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        val mediaId = 1
        viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)

        viewModel.effect.test {
            viewModel.onMediaClick(mediaId)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                TrendingMediaScreenEffect.NavigateToMediaDetails(
                    mediaId
                )
            )
        }

        @Test
        fun `onBackClick emits NavigateBack`() = runTest {
            viewModel = TrendingTvShowsScreenViewModel(manageTvSeriesUseCase, checkIfUserIsLoggedInUseCase,testDispatcher)

            viewModel.onBackClick()

            viewModel.effect.test {
                assertThat(awaitItem()).isEqualTo(TrendingMediaScreenEffect.NavigateBack)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private companion object {
        val genres = listOf(
            Genre(
                id = 1,
                name = "Drama"
            ),
            Genre(
                id = 2,
                name = "Action"
            )
        )
        val tvShows = listOf(
            TvSeries(
                id = 1,
                title = "Breaking Bad",
                posterImageUrl = "https://example.com/breaking_bad.jpg",
                overview = "",
                releaseDate = LocalDate(2008, 1, 20),
                genres = emptyList(),
                imdbRating = 9f,
                seasonsCount = 2,
                rating = 0
            ),
            TvSeries(
                id = 2,
                title = "Game of Thrones",
                posterImageUrl = "https://example.com/image.jpg",
                overview = "",
                releaseDate = LocalDate(2008, 1, 20),
                genres = emptyList(),
                imdbRating = 8f,
                seasonsCount = 7,
                rating = 0
            )
        )
        val media = MediaItem(
            id = 1,
            title = "Test TV Show",
            imageUrl = "https://example.com/image.jpg",
            mediaTypeUi = MediaTypeUi.TV_SHOW
        )
    }
}