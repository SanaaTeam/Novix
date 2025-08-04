package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import entity.Genre
import entity.Movie
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
import usecase.ManageMovieUseCase
import kotlin.time.Duration.Companion.minutes


class TrendingMoviesScreenViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manageMovieUseCase: ManageMovieUseCase
    private lateinit var viewModel: TrendingMoviesScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageMovieUseCase = mockk(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch genres and update state on creation`() = runTest {
        coEvery { manageMovieUseCase.getMovieGenres() } returns genres

        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.genreList).isEqualTo(genres.map { it.toState() })
    }

    @Test
    fun `init should fetch movies and update state on creation`() = runTest {
        coEvery { manageMovieUseCase.getTrendingMovies(any(), any()) } returns movies

        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertThat(viewModel.state.value.mediaList).isNotNull()
        
       
    }

    @Test
    fun `onGenreClick should fetch media and update state with media list for the passed genre when available`() =
        runTest {
            val genreId = 2
            coEvery {
                manageMovieUseCase.getTrendingMovies(any(), genreId)
            } returns movies
            viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onGenreClick(genreId)
            testDispatcher.scheduler.advanceUntilIdle()
            
            assertThat(viewModel.state.value.selectedGenreId).isEqualTo(genreId)
            
            assertThat(viewModel.state.value.mediaList).isNotNull()
        }

    @Test
    fun `fetchGenres should handles error and updates state when throw exception`() = runTest {
        val exceptionMessage = "error"
        coEvery { manageMovieUseCase.getMovieGenres() } throws RuntimeException(exceptionMessage)

        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo(exceptionMessage)
    }

    @Test
    fun `fetchGenres should update isNoInternetConnection state to true when throw NoNetworkException`() =
        runTest {
            coEvery { manageMovieUseCase.getMovieGenres() } throws NoNetworkException()

            viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.state.value.isNoInternetConnection).isTrue()
        }

    @Test
    fun `onSaveIconClick should update state to show bottom sheet when called`() = runTest {
        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSaveIconClick(media)

        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onGenreClick should not fetchMedia when click on same genre`() = runTest {
        coEvery { manageMovieUseCase.getTrendingMovies(any(), any()) } returns listOf(mockk())
        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val selectedGenre = viewModel.state.value.selectedGenreId

        viewModel.onGenreClick(selectedGenre)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { manageMovieUseCase.getTrendingMovies(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        val mediaId = 1
        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)

        viewModel.effect.test {
            viewModel.onMediaClick(mediaId)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                TrendingMediaScreenEffect.NavigateToMediaDetails(
                    mediaId
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        viewModel = TrendingMoviesScreenViewModel(manageMovieUseCase, testDispatcher)

        viewModel.onBackClick()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TrendingMediaScreenEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
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
        val movies = listOf(
            Movie(
                id = 1,
                title = "Breaking Bad",
                posterImageUrl = "https://example.com/breaking_bad.jpg",
                overview = "",
                releaseDate = LocalDate(2008, 1, 20),
                genres = emptyList(),
                imdbRating = 9f,
                duration = 100.minutes,
                trailerUrl = null,
                rating = 0
            ),
            Movie(
                id = 2,
                title = "Game of Thrones",
                posterImageUrl = "https://example.com/image.jpg",
                overview = "",
                releaseDate = LocalDate(2008, 1, 20),
                genres = emptyList(),
                imdbRating = 8f,
                duration = 100.minutes,
                trailerUrl = null,
                rating = 0
            )
        )
        val media = MediaItem(
            id = 1,
            title = "Test Movie",
            imageUrl = "https://example.com/image.jpg",
            mediaTypeUi = MediaTypeUi.MOVIE
        )
    }
}