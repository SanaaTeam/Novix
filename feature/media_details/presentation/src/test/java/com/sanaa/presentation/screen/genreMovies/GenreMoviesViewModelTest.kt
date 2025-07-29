package com.sanaa.presentation.screen.genreMovies

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.sanaa.presentation.model.toUiModel
import entity.Genre
import entity.Movie
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import usecase.ManageMovieUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreMoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: GenreMoviesViewModel
    private lateinit var manageMoviesDetailsUseCase: ManageMovieUseCase

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
        manageMoviesDetailsUseCase = mockk(relaxed = true)
        clearAllMocks()
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        givenHappyViewModel()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        givenHappyViewModel()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        givenHappyViewModel()

        viewModel.onBackClick()

        viewModel.effect.test {
            assertEquals(GenreMoviesEffects.NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit NavigateToMovieDetails effect`() = runTest {
        givenHappyViewModel()

        viewModel.onMovieClick(10)

        viewModel.effect.test {
            assertEquals(GenreMoviesEffects.NavigateToMovieDetails(10), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchMovies should update state with movies`() = runTest {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), any()) } returns movies

        viewModel = GenreMoviesViewModel(
            categoryId = genreList[0].id,
            categoryName = genreList[0].name,
            manageMoviesDetailsUseCase = manageMoviesDetailsUseCase,
            dispatcher = testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.movies
        val items = pagingData.asSnapshot()
        assertEquals(movies.map { it.toUiModel() }, items.take(movies.size))
    }

    private fun givenHappyViewModel() {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), any()) } returns movies
        viewModel = GenreMoviesViewModel(
            categoryId = genreList[0].id,
            categoryName = genreList[0].name,
            manageMoviesDetailsUseCase = manageMoviesDetailsUseCase,
            dispatcher = testDispatcher
        )
        testDispatcher.scheduler.advanceUntilIdle()
    }

    private companion object {
        val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )

        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                posterImageUrl = "",
                genres = emptyList(),
                imdbRating = 8f,
                duration = 120.minutes,
                releaseDate = kotlinx.datetime.LocalDate(2020, 1, 1),
                overview = "",
            ),
            Movie(
                id = 2,
                title = "Movie 2",
                posterImageUrl = "",
                genres = emptyList(),
                imdbRating = 7.5f,
                duration = 110.minutes,
                releaseDate = kotlinx.datetime.LocalDate(2019, 1, 1),
                overview = "",
            )
        )
    }
}
