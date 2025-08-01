package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.state.mapper.toState
import entity.Genre
import entity.Movie
import entity.TvSeries
import io.mockk.coEvery
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
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatedMediaScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manageMovieUseCase: ManageMovieUseCase
    private lateinit var manageTvSeriesUseCase: ManageTvSeriesUseCase
    private lateinit var viewModel: TopRatedMediaScreenViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageMovieUseCase = mockk(relaxed = true)
        manageTvSeriesUseCase = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch movie genres and update state`() = runTest {
        coEvery { manageMovieUseCase.getMovieGenres() } returns genres

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.movieGenres).isEqualTo(genres.map { it.toState() })
    }

    @Test
    fun `init should fetch tv show genres and update state`() = runTest {
        coEvery { manageTvSeriesUseCase.getSeriesGenres() } returns tvGenres

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.tvShowGenres).isEqualTo(tvGenres.map { it.toState() })
    }

    @Test
    fun `fetchMovies should update media list with movies`() = runTest {
        coEvery { manageMovieUseCase.getTopRatedMovies(any(), any()) } returns movies

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.movieList
        val items = pagingData.asSnapshot()
        assertThat(items.take(movies.size)).isEqualTo(movies.map { it.toState() })
    }

    @Test
    fun `fetchTvShows should update media list with tv shows`() = runTest {
        coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(any(), any()) } returns tvShows

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.tvShowList
        val items = pagingData.asSnapshot()
        assertThat(items.take(tvShows.size)).isEqualTo(tvShows.map { it.toState() })
    }

    @Test
    fun `onMovieGenreClick should fetch movies for selected genre`() = runTest {
        val genreId = 2
        coEvery { manageMovieUseCase.getTopRatedMovies(any(), genreId) } returns movies

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onMovieGenreClick(genreId)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.movieList
        val items = pagingData.asSnapshot()
        assertThat(items.take(movies.size)).isEqualTo(movies.map { it.toState() })
    }

    @Test
    fun `onTvShowGenreClick should fetch tv shows for selected genre`() = runTest {
        val genreId = 3
        coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(any(), genreId) } returns tvShows

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onTvShowGenreClick(genreId)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.tvShowList
        val items = pagingData.asSnapshot()
        assertThat(items.take(tvShows.size)).isEqualTo(tvShows.map { it.toState() })
    }

    @Test
    fun `fetchMovieGenres should set error message on failure`() = runTest {
        val errorMsg = "error"
        coEvery { manageMovieUseCase.getMovieGenres() } throws RuntimeException(errorMsg)

        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo(errorMsg)
    }

    @Test
    fun `onSaveIconClick should show bottom sheet`() = runTest {
        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSaveIconClick(media)

        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails`() = runTest {
        val id = 10
        val type = MediaType.MOVIE
        viewModel =
            TopRatedMediaScreenViewModel(manageMovieUseCase, manageTvSeriesUseCase, testDispatcher)

        viewModel.effect.test {
            viewModel.onMediaClick(id, type)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(MediaTabScreenEffect.NavigateToMediaDetails(id, type))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onBackClick should emit NavigateBack`() = runTest {
        viewModel =
            TopRatedMediaScreenViewModel(
                manageMovieUseCase,
                manageTvSeriesUseCase,
                testDispatcher
            )

        viewModel.onBackClick()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MediaTabScreenEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        val genres = listOf(Genre(1, "Action"), Genre(2, "Drama"))
        val tvGenres = listOf(Genre(3, "Sci-Fi"))
        val movies = listOf(
            Movie(
                1,
                "Movie 1",
                "",
                emptyList(),
                8f,
                120.minutes,
                LocalDate(2020, 1, 1),
                "",
                ""
            ),
            Movie(2, "Movie 2", "", emptyList(), 9f, 110.minutes, LocalDate(2019, 1, 1), "", "")
        )
        val tvShows = listOf(
            TvSeries(1, "Show 1", "", LocalDate(2021, 1, 1), emptyList(), 9f, "", 3),
            TvSeries(2, "Show 2", "", LocalDate(2022, 1, 1), emptyList(), 8f, "", 2)
        )
        val media = MediaItem(1, "Media", "", mediaType = MediaType.MOVIE)
    }
}