package com.sanaa.presentation.screen.trendingTvShowScreen

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import com.sanaa.presentation.state.mapper.toState
import entity.Genre
import entity.TvShow
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
import service.VodStringProvider
import usecase.ManageTvShowUseCase

class TrendingTvShowsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manageTvShowUseCase: ManageTvShowUseCase
    private val stringProvider: VodStringProvider = mockk(relaxed = true)

    private lateinit var viewModel: TrendingTvShowsScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageTvShowUseCase = mockk(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch genres and update state on creation`() = runTest {
        coEvery { manageTvShowUseCase.getTvShowGenres() } returns genres

        viewModel =
            TrendingTvShowsScreenViewModel(manageTvShowUseCase, stringProvider, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.genreList).isEqualTo(genres.map { it.toState() })
    }

    @Test
    fun `init should fetch tv show and update state on creation`() = runTest {
        coEvery { manageTvShowUseCase.getTrendingTvShows(any(), any()) } returns tvShows

        viewModel =
            TrendingTvShowsScreenViewModel(manageTvShowUseCase, stringProvider, testDispatcher)
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
                manageTvShowUseCase.getTrendingTvShows(any(), genreId)
            } returns tvShows
            viewModel =
                TrendingTvShowsScreenViewModel(manageTvShowUseCase, stringProvider, testDispatcher)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onGenreClick(genreId)
            testDispatcher.scheduler.advanceUntilIdle()
            val pagingData = viewModel.state.value.mediaList
            val items = pagingData.asSnapshot()

            assertThat(items.take(tvShows.size)).isEqualTo(tvShows.map { it.toState() })
        }

    @Test
    fun `onGenreClick should not fetchMedia when click on same genre`() = runTest {
        coEvery { manageTvShowUseCase.getTrendingTvShows(any(), any()) } returns listOf(mockk())
        viewModel =
            TrendingTvShowsScreenViewModel(manageTvShowUseCase, stringProvider, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val selectedGenre = viewModel.state.value.selectedGenreId

        viewModel.onGenreClick(selectedGenre)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { manageTvShowUseCase.getTrendingTvShows(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        val mediaId = 1
        viewModel =
            TrendingTvShowsScreenViewModel(manageTvShowUseCase, stringProvider, testDispatcher)

        viewModel.effect.test {
            viewModel.onMediaClick(mediaId)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                TrendingTvShowsScreenEffect.NavigateToTvShowDetails(
                    mediaId
                )
            )
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
            TvShow(
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
            TvShow(
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
        val media = MediaItemUiState(
            id = 1,
            title = "Test TV Show",
            imageUrl = "https://example.com/image.jpg",
            mediaTypeUiState = MediaTypeUiState.TV_SHOW
        )
    }
}