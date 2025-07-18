package com.sanaa.presentation.screens.category

import app.cash.turbine.test
import com.sanaa.presentation.model.toUiModel
import com.sanaa.presentation.screen.movie_categories.MovieCategoriesScreenEffects
import com.sanaa.presentation.screen.movie_categories.MovieCategoriesScreenUiState
import com.sanaa.presentation.screen.movie_categories.MovieCategoriesViewModel
import details.usecase.ManageMovieDetailsUseCase
import entity.Genre
import entity.Movie
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieCategoriesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: MovieCategoriesViewModel

    private val manageMoviesDetailsUseCase: ManageMovieDetailsUseCase = mockk()
    private val categoryId = Genre.DRAMA

    private val movieList = listOf(
        Movie(
            id = 1,
            title = "Movie1",
            posterImageUrl = "url1",
            imdbRating = 8f,
            duration = 120,
            releaseDate = LocalDate.parse("2021-06-01"),
            overview = "Overview1",
            trailerUrl = "https://youtube.com/trailer1",
            genres = listOf(Genre.ACTION)
        ),
        Movie(
            id = 2,
            title = "Movie2",
            posterImageUrl = "url2",
            imdbRating = 7f,
            duration = 90,
            releaseDate = LocalDate.parse("2024-06-01"),
            overview = "Overview2",
            trailerUrl = "https://youtube.com/trailer2",
            genres = listOf(Genre.ACTION)
        )
    )

    @BeforeAll
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterAll
    fun resetDispatcher() {
        Dispatchers.resetMain()
    }

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `when init then should load movies and update state`() = runTest(testDispatcher) {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns movieList

        viewModel = MovieCategoriesViewModel(Genre.DRAMA, manageMoviesDetailsUseCase)

        viewModel.state.test {
            var currentState = awaitItem()
            while (currentState.isLoading) {
                currentState = awaitItem()
            }

            val expectedState = MovieCategoriesScreenUiState(
                title = Genre.DRAMA,
                movies = movieList.map { it.toUiModel() },
                isLoading = false,
                error = null,
                showBottomSheet = false
            )

            assertEquals(expectedState, currentState)
        }
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns emptyList()

        viewModel = MovieCategoriesViewModel(categoryId, manageMoviesDetailsUseCase)
        advanceUntilIdle()

        viewModel.onSaveIconClick()

        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns emptyList()

        viewModel = MovieCategoriesViewModel(categoryId, manageMoviesDetailsUseCase)
        advanceUntilIdle()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns emptyList()

        viewModel = MovieCategoriesViewModel(categoryId, manageMoviesDetailsUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClick()
            assertEquals(MovieCategoriesScreenEffects.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onMovieClick should emit NavigateToMovieDetails effect`() = runTest {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns emptyList()

        viewModel = MovieCategoriesViewModel(categoryId, manageMoviesDetailsUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onMovieClick(10)
            assertEquals(MovieCategoriesScreenEffects.NavigateToMovieDetails(10), awaitItem())
        }
    }
    @Test
    fun `when getMoviesByCategory throws exception then should update state with error`() = runTest(testDispatcher) {
        val exception = RuntimeException()

        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } throws exception

        viewModel = MovieCategoriesViewModel(Genre.DRAMA, manageMoviesDetailsUseCase)

        viewModel.state.test {
            var currentState = awaitItem()
            while (currentState.isLoading) {
                currentState = awaitItem()
            }

            val expectedState = MovieCategoriesScreenUiState(
                title = null,
                movies = emptyList(),
                isLoading = false,
                error = exception.message,
                showBottomSheet = false
            )

            assertEquals(expectedState, currentState)
        }
    }

}
