import androidx.paging.PagingSource
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.homeScreen.HomeScreenEffect
import com.sanaa.presentation.screen.homeScreen.HomeScreenViewModel
import com.sanaa.presentation.state.MediaTypeUi
import entity.Genre
import entity.MediaHistoryItem
import entity.Movie
import entity.TvSeries
import entity.User
import exceptions.NoLoggedInUserException
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import kotlin.time.Duration.Companion.minutes

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {

    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk(relaxed = true)
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase =
        mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)

    private lateinit var viewModel: HomeScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { manageMovieUseCase.getPopularMovies(any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getPopularSeries(any()) } returns emptyList()
        coEvery { manageMovieUseCase.getTopRatedMovies(any(), any()) } returns emptyList()
        coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(any(), any()) } returns emptyList()
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserException()
        coEvery {
            manageWatchedMediaHistoryUseCase.getMediaHistory(
                any(),
                any(),
                any()
            )
        } returns flowOf(emptyList())
        coEvery { manageMovieUseCase.getMovieGenres() } returns emptyList()
        coEvery { manageMovieUseCase.getUpcomingMovies(any(), any()) } returns emptyList()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initializeViewModel() {
        viewModel = HomeScreenViewModel(
            manageMovieUseCase,
            manageTvSeriesUseCase,
            manageWatchedMediaHistoryUseCase,
            getLoggedInUserUseCase,
            checkIfUserIsLoggedInUseCase,
            testDispatcher
        )
    }

    @Test
    fun `init should fetch popular media and update state on success`() = runTest(testDispatcher) {
        val popularMovies = listOf(dummyMovie.copy(id = 1))
        val popularTvSeries = listOf(dummyTvSeries.copy(id = 2))

        coEvery { manageMovieUseCase.getPopularMovies(1) } returns popularMovies
        coEvery { manageTvSeriesUseCase.getPopularSeries(1) } returns popularTvSeries

        initializeViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.popularMedia).hasSize(2)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `init should return empty watched history when user is not logged in`() =
        runTest(testDispatcher) {
            coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserException()

            initializeViewModel()
            advanceUntilIdle()

            val finalState = viewModel.state.value
            assertThat(finalState.continueWatchingMedia).isEmpty()
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
            viewModel.onMediaClick(101, MediaTypeUi.MOVIE)
            assertThat(awaitItem()).isEqualTo(
                HomeScreenEffect.NavigateToMediaDetails(
                    101,
                    MediaTypeUi.MOVIE
                )
            )
        }
    }

    @Test
    fun `onMovieGenreClick should update selected ID`() = runTest(testDispatcher) {
        val newGenreId = 12
        initializeViewModel()
        viewModel.onMovieGenreClick(newGenreId)
        assertThat(viewModel.state.value.movieSelectedGenreId).isEqualTo(newGenreId)
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
    fun `onRetryClick should re-fetch all data`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onRetryClick()
        advanceUntilIdle()
        coVerify(exactly = 2) { manageMovieUseCase.getPopularMovies(any()) }
        coVerify(exactly = 2) { manageTvSeriesUseCase.getPopularSeries(any()) }
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

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).isEqualTo(upcomingMovies)
        assertThat(page.nextKey).isEqualTo(2)
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
    }
}