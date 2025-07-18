package com.sanaa.presentation.screen.movie_categories

import app.cash.turbine.test
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
    fun `init should load movies and update state`() = runTest(testDispatcher) {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any()) } returns movieList

        viewModel = MovieCategoriesViewModel(Genre.DRAMA, manageMoviesDetailsUseCase)

        val expectedState = MovieCategoriesScreenUiState(
            title = Genre.DRAMA,
            movies = movieList.map {
                MovieCardUiModel(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.posterImageUrl,
                    rating = it.imdbRating
                )
            },
            isLoading = false,
            error = null,
            showBottomSheet = false
        )

        viewModel.state.test {
            skipItems(1)

            val actualState = awaitItem()
            assertEquals(expectedState, actualState)
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

}
