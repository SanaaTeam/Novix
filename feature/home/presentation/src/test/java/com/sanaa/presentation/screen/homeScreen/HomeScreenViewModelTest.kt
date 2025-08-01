package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingSource
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.state.MediaType
import entity.Genre
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageHistoryUseCase
import kotlin.time.Duration.Companion.minutes

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {

    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk(relaxed = true)
    private val manageHistoryUseCase: ManageHistoryUseCase = mockk(relaxed = true)

    private lateinit var viewModel: HomeScreenViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { manageMovieUseCase.getPopularMovies(any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getPopularSeries(any()) } returns emptyList()
        coEvery { manageMovieUseCase.getTopRatedMovies(any(), any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(any(), any()) } returns emptyList()
        coEvery { manageHistoryUseCase.getWatchedMoviesHistory(any(), any()) } returns emptyList()
        coEvery { manageHistoryUseCase.getWatchedSeriesHistory(any(), any()) } returns emptyList()
        coEvery { manageMovieUseCase.getMovieGenres() } returns emptyList()
        coEvery { manageMovieUseCase.getUpcomingMovies(any(), any()) } returns emptyList()
    }

    private fun initializeViewModel() {
        viewModel = HomeScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            manageHistoryUseCase
        )
    }

    @Test
    fun `onRetryClick should re-fetch all data and update state successfully`() =
        runTest(testDispatcher) {
            // Given
            val popular = listOf(dummyMovie.copy(id = 1))
            val topRated = listOf(dummyMovie.copy(id = 2))
            val watched = listOf(dummyMovie.copy(id = 3))
            val genres = listOf(dummyGenre)

            coEvery { manageMovieUseCase.getPopularMovies(1) } returns popular
            coEvery { manageTvSeriesUseCase.getPopularSeries(1) } returns emptyList()
            coEvery { manageMovieUseCase.getTopRatedMovies(1, null) } returns topRated
            coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(1, null) } returns emptyList()
            coEvery { manageHistoryUseCase.getWatchedMoviesHistory(10, null) } returns watched
            coEvery { manageHistoryUseCase.getWatchedSeriesHistory(10, null) } returns emptyList()
            coEvery { manageMovieUseCase.getMovieGenres() } returns genres

            initializeViewModel()

            // When
            viewModel.onRetryClick()

            // Then
            viewModel.state.test {
                val finalState = awaitItem().let {
                    var current = it
                    while (
                        current.popularMedia.isEmpty() ||
                        current.topRatingMedia.isEmpty() ||
                        current.continueWatchingMedia.isEmpty() ||
                        current.movieGenres.isEmpty()
                    ) {
                        current = awaitItem()
                    }
                    current
                }

                assertThat(finalState.isNoInternet).isFalse()
                assertThat(finalState.popularMedia).hasSize(1)
                assertThat(finalState.topRatingMedia).hasSize(1)
                assertThat(finalState.continueWatchingMedia).hasSize(1)
                assertThat(finalState.movieGenres).hasSize(1)
            }
        }

    @Test
    fun `onMovieGenreClick should update selected ID`() = runTest(testDispatcher) {
        // Given
        val newGenreId = 12
        initializeViewModel()

        // When
        viewModel.onMovieGenreClick(newGenreId)

        // Then
        assertThat(viewModel.state.value.movieSelectedGenreId).isEqualTo(newGenreId)
    }

    @Test
    fun `onMoviesCardClicked should emit NavigateToMoviesScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onMoviesCardClicked()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToMoviesScreen)
            }
        }

    @Test
    fun `onTvShowsCardClicked should emit NavigateToTvShowsScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onTvShowsCardClicked()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTvShowsScreen)
            }
        }

    @Test
    fun `onPeopleCardClicked should emit NavigateToPeopleScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onPeopleCardClicked()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToPeopleScreen)
            }
        }

    @Test
    fun `onShowAllTopRatingClicked should emit NavigateToTopRatingMediaScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onShowAllTopRatingClicked()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToTopRatingMediaScreen)
            }
        }

    @Test
    fun `onShowAllContinueWatchingClicked should emit NavigateToWatchedMediaScreen effect`() =
        runTest(testDispatcher) {
            initializeViewModel()
            viewModel.effect.test {
                viewModel.onShowAllContinueWatchingClicked()
                assertThat(awaitItem()).isEqualTo(HomeScreenEffect.NavigateToWatchedMediaScreen)
            }
        }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.effect.test {
            viewModel.onMediaClick(101, MediaType.MOVIE)
            assertThat(awaitItem()).isEqualTo(
                HomeScreenEffect.NavigateToMediaDetails(
                    101,
                    MediaType.MOVIE
                )
            )
        }
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onSaveIconClick(mockk())
        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet should set showBottomSheet to false`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onSaveIconClick(mockk())
        viewModel.onDismissBottomSheet()
        assertThat(viewModel.state.value.showBottomSheet).isFalse()
    }

    @Test
    fun `onErrorLoadingData should set isNoInternet to true for NoNetworkException`() = runTest {
        // Given
        coEvery { manageMovieUseCase.getPopularMovies(any()) } throws NoNetworkException()

        // When
        initializeViewModel()
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertThat(finalState.isNoInternet).isTrue()
        assertThat(finalState.isLoading).isFalse()
        assertThat(finalState.errorMessage).isNull()
    }

    @Test
    fun `createUpcomingMoviesPagingDataSource should load movies successfully from use case`() =
        runTest {
            // Given
            val upcomingMovies = listOf(dummyMovie.copy(id = 123, title = "Upcoming Movie"))
            coEvery {
                manageMovieUseCase.getUpcomingMovies(
                    page = 1,
                    genreId = null
                )
            } returns upcomingMovies
            initializeViewModel()
            val pagingSource = viewModel.createUpcomingMoviesPagingDataSource(genreId = null)

            // When
            val result = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )

            // Then
            val expected = PagingSource.LoadResult.Page(
                data = upcomingMovies,
                prevKey = null,
                nextKey = 2
            )
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `createUpcomingMoviesPagingDataSource should return an error when use case throws exception`() =
        runTest {
            // Given
            val error = RuntimeException("Failed to fetch")
            coEvery { manageMovieUseCase.getUpcomingMovies(page = 1, genreId = null) } throws error
            initializeViewModel()
            val pagingSource = viewModel.createUpcomingMoviesPagingDataSource(genreId = null)

            // When
            val result = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )

            // Then
            assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
            assertThat((result as PagingSource.LoadResult.Error).throwable).isEqualTo(error)
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
            trailerUrl = null,
            rating = 0
        )
        val dummyTvSeries = TvSeries(
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
    }
}