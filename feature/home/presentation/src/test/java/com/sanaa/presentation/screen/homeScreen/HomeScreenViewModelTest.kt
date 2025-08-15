package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingSource
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.state.MediaTypeUiState
import entity.Genre
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import entity.User
import exceptions.NoLoggedInUserException
import io.mockk.coEvery
import io.mockk.coVerify
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
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import kotlin.test.Ignore
import kotlin.time.Duration.Companion.minutes

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {

    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvShowUseCase: ManageTvShowUseCase = mockk(relaxed = true)
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase =
        mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk(relaxed = true)

    private lateinit var viewModel: HomeScreenViewModel
    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { manageMovieUseCase.getPopularMovies(any()) } returns emptyList()
        coEvery { manageTvShowUseCase.getPopularTvShows(any()) } returns emptyList()
        coEvery { manageMovieUseCase.getTopRatedMovies(any(), any()) } returns emptyList()
        coEvery { manageTvShowUseCase.getTopRatedTvShows(any(), any()) } returns emptyList()
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserException()
        coEvery {
            manageWatchedMediaHistoryUseCase.getMediaHistory(any(), any(), any())
        } returns flowOf(emptyList())
        coEvery { manageMovieUseCase.getMovieGenres() } returns emptyList()
        coEvery { manageMovieUseCase.getUpcomingMovies(any(), any()) } returns emptyList()
    }

    private fun initializeViewModel() {
        viewModel = HomeScreenViewModel(
            manageMovieUseCase,
            manageTvShowUseCase,
            manageWatchedMediaHistoryUseCase,
            getLoggedInUserUseCase,
            checkIfUserIsLoggedInUseCase,
            stringProvider,
            testDispatcher,
        )
    }

    @Test
    fun `init should fetch popular media and update state on success`() = runTest(testDispatcher) {
        // Given
        val popularMovies = listOf(dummyMovie.copy(id = 1))
        val popularTvShows = listOf(dummyTvShow.copy(id = 2))

        coEvery { manageMovieUseCase.getPopularMovies(1) } returns popularMovies
        coEvery { manageTvShowUseCase.getPopularTvShows(1) } returns popularTvShows

        // When
        initializeViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val successState = awaitItem().let {
                var current = it
                while (current.popularMedia.size != 2) {
                    current = awaitItem()
                }
                current
            }

            assertThat(successState.popularMedia).hasSize(2)
            assertThat(successState.isLoadingPopular).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init should fetch top-rated media and update state on success`() =
        runTest(testDispatcher) {
            // Given
            val topRatedMovies = listOf(dummyMovie.copy(id = 3))
            coEvery { manageMovieUseCase.getTopRatedMovies(1, null) } returns topRatedMovies

            // When
            initializeViewModel()
            advanceUntilIdle()
            viewModel.state.test {
                val state = awaitItem().let {
                    var current = it
                    while (current.topRatingMedia.isEmpty()) {
                        current = awaitItem()
                    }
                    current
                }
                assertThat(state.topRatingMedia).hasSize(1)
            }
        }


    @Test
    fun `init should fetch watched history when user is logged in`() = runTest(testDispatcher) {
        val historyItems = listOf(dummyHistoryItem)
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns flowOf(dummyUser)
        coEvery {
            manageWatchedMediaHistoryUseCase.getMediaHistory(
                dummyUser.username,
                null,
                null
            )
        } returns flowOf(historyItems)
        initializeViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.state.test {
            val successState = awaitItem().let {
                var current = it
                while (current.continueWatchingMedia.isEmpty()) {
                    current = awaitItem()
                }
                current
            }
            assertThat(successState.continueWatchingMedia).hasSize(1)
        }
    }

    @Test
    fun `init should return empty watched history when user is not logged in`() =
        runTest(testDispatcher) {
            coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserException()
            initializeViewModel()
            testDispatcher.scheduler.advanceUntilIdle()
            val finalState = viewModel.state.value
            assertThat(finalState.continueWatchingMedia).isEmpty()
        }

    @Test
    fun `init should fetch genres and update state on success`() = runTest(testDispatcher) {
        val genres = listOf(dummyGenre)
        coEvery { manageMovieUseCase.getMovieGenres() } returns genres
        initializeViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.state.test {
            val state = awaitItem().let {
                var current = it
                while (current.movieGenres.isEmpty()) {
                    current = awaitItem()
                }
                current
            }
            assertThat(state.movieGenres).hasSize(1)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMoviesCardClicked should emit NavigateToTrendingMoviesScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onMoviesCardClick()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTrendingMoviesScreen)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onTvShowsCardClicked should emit NavigateToTrendingTvShowsScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onTvShowsCardClick()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTrendingTvShowsScreen)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onPeopleCardClicked should emit NavigateToTrendingPeopleScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onPeopleCardClick()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTrendingPeopleScreen)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onShowAllTopRatingClicked should emit NavigateToTopRatingMediaScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onShowAllTopRatingClick()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTopRatingMediaScreen)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onShowAllContinueWatchingClicked should emit NavigateToWatchedMediaScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onShowAllContinueWatchingClick()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToWatchedMediaScreen)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.effect.test {
            viewModel.onMediaClick(101, MediaTypeUiState.MOVIE)
            assertThat(awaitItem()).isEqualTo(
                HomeScreenEffect.NavigateToMediaDetails(
                    101,
                    MediaTypeUiState.MOVIE
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMovieGenreClick should update selected ID`() = runTest(testDispatcher) {
        val newGenreId = 12
        initializeViewModel()
        viewModel.onMovieGenreClick(newGenreId)
        assertThat(viewModel.state.value.movieSelectedGenreId).isEqualTo(newGenreId)
    }

    @Ignore
    @Test
    fun `onSaveIconClick should set showLoginBottomSheet to true`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onSaveIconClick(mockk())
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet should set showBottomSheet to false`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onSaveIconClick(mockk())
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onRetryClick should re-fetch all data`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onRetryClick()
        advanceUntilIdle()
        coVerify(exactly = 2) { manageMovieUseCase.getPopularMovies(any()) }
        coVerify(exactly = 2) { manageTvShowUseCase.getPopularTvShows(any()) }
    }

    @Test
    fun `createUpcomingMoviesPagingDataSource should load movies successfully`() = runTest {
        val upcomingMovies = listOf(dummyMovie)
        coEvery {
            manageMovieUseCase.getUpcomingMovies(
                page = 1,
                genreId = null
            )
        } returns upcomingMovies
        initializeViewModel()
        val pagingSource = viewModel.createUpcomingMoviesPagingDataSource(genreId = null)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 20, placeholdersEnabled = false)
        )
        val expected =
            PagingSource.LoadResult.Page(data = upcomingMovies, prevKey = null, nextKey = 2)
        assertThat(result).isEqualTo(expected)
    }

    private companion object {
        val dummyMovie = Movie(
            id = 1,
            title = "Dummy Movie",
            posterImageUrl = "",
            genres = emptyList(),
            imdbRating = 7.5f,
            duration = 120.minutes,
            releaseDate = LocalDate(2020, 2, 1),
            overview = "",
            trailerUrl = "",
            rating = 0
        )
        val dummyTvShow = TvShow(
            id = 2,
            title = "Dummy Series",
            posterImageUrl = "",
            overview = "",
            releaseDate = LocalDate(2020, 2, 1),
            genres = emptyList(),
            imdbRating = 8.0f,
            seasonsCount = 3,
            rating = 0
        )
        val dummyGenre = Genre(id = 28, name = "Action")
        val dummyUser = User(id = 42L, name = "Test User", username = "test-user", "ImageUrl")
        val dummyHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "",
            mediaType = MediaType.MOVIE,
            genres = emptyList(),
            lastWatchedAt = 0L
        )
    }
}